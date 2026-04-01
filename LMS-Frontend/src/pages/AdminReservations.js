import React, { useEffect, useState } from "react";

function AdminReservations() {
  const [reservations, setReservations] = useState([]);

  const getAuthHeaders = () => {
    const token = localStorage.getItem("token");
    return {
      "Content-Type": "application/json",
      "Authorization": token ? `Bearer ${token}` : ""
    };
  };

  const fetchAllReservations = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/reservations/getallusers", {
        headers: getAuthHeaders()
      });
      if (res.ok) {
        const data = await res.json();
        setReservations(data || []);
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchAllReservations();
  }, []);

  return (
    <div className="container mt-5">
      <h2 style={{ color: "#1e3c72", fontWeight: "700" }} className="mb-4">Global Reservations</h2>
      
      <div className="card glass-card p-4">
        <div className="table-responsive">
          <table className="table table-hover align-middle">
            <thead className="table-dark">
              <tr>
                <th>User</th>
                <th>Book</th>
                <th>Reserved On</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {reservations.map(r => (
                <tr key={r.reservationId}>
                  <td>
                    <div className="fw-bold">{r.user?.name}</div>
                    <div className="small text-muted">{r.user?.email}</div>
                  </td>
                  <td>{r.book?.title}</td>
                  <td>{r.reservationDate}</td>
                  <td><span className="badge bg-info">{r.status}</span></td>
                </tr>
              ))}
              {reservations.length === 0 && <tr><td colSpan="4" className="text-center py-4">No active reservations.</td></tr>}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default AdminReservations;
