import React, { useEffect, useState } from "react";

function Reservations() {
  const [reservations, setReservations] = useState([]);
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    if (userId) fetchReservations();
  }, [userId]);

  const getAuthHeaders = () => {
    const token = localStorage.getItem("token");
    return {
      "Content-Type": "application/json",
      "Authorization": token ? `Bearer ${token}` : ""
    };
  };

  const fetchReservations = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/reservations/getuser/${userId}`, {
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

  const handleCancel = async (id) => {
    if (window.confirm("Cancel this reservation?")) {
      try {
        const res = await fetch(`http://localhost:8080/api/reservations/canceluser/${id}`, {
          method: "DELETE",
          headers: getAuthHeaders()
        });
        if (res.ok) {
          alert("Reservation Cancelled");
          fetchReservations();
        }
      } catch (err) {
        alert("Cancellation failed.");
      }
    }
  };

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 style={{ color: "#1e3c72", fontWeight: "700" }} className="m-0">My Reservations</h2>
        <button className="btn btn-primary-custom" onClick={() => window.location.href='/books'}>
          Reserve Book
        </button>
      </div>

      <div className="card glass-card p-4">
        {reservations.length === 0 ? (
          <p className="text-center text-muted my-4">No active reservations found.</p>
        ) : (
          <div className="table-responsive">
            <table className="table table-hover align-middle">
              <thead className="table-dark">
                <tr>
                  <th>Book Title</th>
                  <th>Reserved On</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {reservations.map(r => (
                  <tr key={r.reservationId}>
                    <td>{r.book?.title}</td>
                    <td>{r.reservationDate}</td>
                    <td><span className="badge bg-info">{r.status}</span></td>
                    <td>
                      <button className="btn btn-sm btn-outline-danger" onClick={() => handleCancel(r.reservationId)}>Cancel</button>
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

export default Reservations;
