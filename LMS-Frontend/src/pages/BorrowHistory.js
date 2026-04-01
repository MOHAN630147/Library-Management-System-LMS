import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function BorrowHistory() {
  const [history, setHistory] = useState([]);
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    if (userId) fetchHistory();
  }, [userId]);

  const getAuthHeaders = () => {
    const token = localStorage.getItem("token");
    return {
      "Content-Type": "application/json",
      "Authorization": token ? `Bearer ${token}` : ""
    };
  };

  const fetchHistory = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/borrow/Borrowhistoryofuser/${userId}`, {
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

  const handleMarkAsLost = async (borrowId) => {
    if (window.confirm("Are you sure you want to mark this book as lost? Overdue fines will still apply.")) {
      try {
        const res = await fetch(`http://localhost:8080/api/borrow/lost/${borrowId}`, {
          method: "PUT",
          headers: getAuthHeaders()
        });
        if (res.ok) {
          alert("Book marked as lost.");
          fetchHistory();
        }
      } catch (err) {
        alert("Action failed.");
      }
    }
  };

  const calculateFine = async (borrowId) => {
    try {
      const res = await fetch(`http://localhost:8080/api/borrow/fine/${borrowId}`, {
        headers: getAuthHeaders()
      });
      if (res.ok) {
        alert("Latest fine calculated. Please refresh history.");
        fetchHistory();
      }
    } catch (err) {
      alert("Fine calculation failed.");
    }
  }
  
  const handleReturn = async (borrowId) => {
    if (window.confirm("Are you sure you want to return this book?")) {
      try {
        const res = await fetch(`http://localhost:8080/api/borrow/return/${borrowId}`, {
          method: "PUT",
          headers: getAuthHeaders()
        });
        if (res.ok) {
          alert("Book Returned Successfully");
          fetchHistory();
        }
      } catch (err) {
        alert("Return failed.");
      }
    }
  };

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 style={{ color: "#1e3c72", fontWeight: "700" }} className="m-0">My Borrowing History</h2>
        <button className="btn btn-primary-custom" onClick={() => navigate('/books')}>
          Borrow Book
        </button>
      </div>

      <div className="card glass-card p-4">
        {history.length === 0 ? (
          <p className="text-center text-muted my-4">No borrow records found.</p>
        ) : (
          <div className="table-responsive">
            <table className="table table-hover align-middle">
              <thead className="table-dark">
                <tr>
                  <th>Book Title</th>
                  <th>Borrowed On</th>
                  <th>Return Deadline</th>
                  <th>Status</th>
                  <th>Fine (Rs)</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {history.map(h => (
                  <tr key={h.borrowId}>
                    <td>{h.book?.title}</td>
                    <td>{h.borrowDate}</td>
                    <td>{h.actualReturnDate}</td>
                    <td>
                      <span className={`badge ${h.status === 'RETURNED' ? 'bg-success' : h.status === 'LOST' ? 'bg-dark' : 'bg-primary'}`}>
                        {h.status}
                      </span>
                      {h.returnRequested && h.status === 'BORROWED' && (
                        <div className="mt-1">
                          <span className="badge bg-danger animate-pulse" style={{ fontSize: '0.7rem' }}>RETURN REQUESTED BY ADMIN</span>
                        </div>
                      )}
                    </td>
                    <td className="fw-bold text-danger">₹{h.fine || 0}</td>
                    <td>
                      {h.status === 'BORROWED' && (
                        <>
                          <button className="btn btn-sm btn-outline-success me-2" onClick={() => handleReturn(h.borrowId)}>Return Book</button>
                          <button className="btn btn-sm btn-outline-warning me-2" onClick={() => calculateFine(h.borrowId)}>Check Fine</button>
                          <button className="btn btn-sm btn-outline-danger" onClick={() => handleMarkAsLost(h.borrowId)}>Mark Lost</button>
                        </>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default BorrowHistory;
