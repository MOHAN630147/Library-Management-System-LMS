import React, { useEffect, useState } from "react";

function AdminBorrowHistory() {
  const [history, setHistory] = useState([]);

  const getAuthHeaders = () => {
    const token = localStorage.getItem("token");
    return {
      "Content-Type": "application/json",
      "Authorization": token ? `Bearer ${token}` : ""
    };
  };

  const fetchAllHistory = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/borrow/getallborrowrecords", {
        headers: getAuthHeaders()
      });
      if (res.ok) {
        const data = await res.json();
        setHistory(data || []);
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchAllHistory();
  }, []);

  const notifyReturn = async (borrowId) => {
    try {
      const res = await fetch(`http://localhost:8080/api/borrow/request-return/${borrowId}`, {
        method: "PUT",
        headers: getAuthHeaders()
      });
      if (res.ok) {
        alert("Return notification sent to user successfully!");
        fetchAllHistory();
      }
    } catch (err) {
      alert("Failed to send return notification");
    }
  };

  return (
    <div className="container mt-5">
      <h2 style={{ color: "#1e3c72", fontWeight: "700" }} className="mb-4">Global Borrowing Records</h2>

      <div className="card glass-card p-4">
        <div className="table-responsive">
          <table className="table table-hover align-middle">
            <thead className="table-dark">
              <tr>
                <th>User</th>
                <th>Book</th>
                <th>Borrowed On</th>
                <th>Deadline</th>
                <th>Status</th>
                <th>Fine (₹)</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {history.map(h => (
                <tr key={h.borrowId}>
                  <td>
                    <div className="fw-bold">{h.user?.name}</div>
                    <div className="small text-muted">{h.user?.email}</div>
                  </td>
                  <td>{h.book?.title}</td>
                  <td>{h.borrowDate}</td>
                  <td>{h.actualReturnDate}</td>
                  <td>
                    <span className={`badge ${h.status === 'RETURNED' ? 'bg-success' : h.status === 'LOST' ? 'bg-dark' : 'bg-primary'}`}>
                      {h.status}
                    </span>
                    {h.returnRequested && (
                      <div className="mt-1">
                        <span className="badge bg-warning text-dark" style={{ fontSize: '0.65rem' }}>RETURN REQUESTED</span>
                      </div>
                    )}
                  </td>
                  <td className="text-danger fw-bold">₹{h.fine || 0}</td>
                  <td>
                    {h.status === 'BORROWED' && (
                      <div className="d-flex flex-column gap-1">
                        <button className="btn btn-sm btn-outline-warning" style={{ fontSize: '0.75rem' }} onClick={() => notifyReturn(h.borrowId)}>
                          Notify Return
                        </button>
                      </div>
                    )}
                  </td>
                </tr>
              ))}
              {history.length === 0 && <tr><td colSpan="7" className="text-center py-4">No records found.</td></tr>}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default AdminBorrowHistory;
