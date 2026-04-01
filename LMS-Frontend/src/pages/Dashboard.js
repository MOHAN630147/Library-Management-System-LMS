
import { Link, useNavigate } from "react-router-dom";

function Dashboard() {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role") || "";
  const name = localStorage.getItem("name") || "";
  const navigate = useNavigate();

  if (!token) {
    return (
      <div className="auth-wrapper">
         <div className="auth-card p-5 text-center">
            <i className="bi bi-shield-lock text-danger display-1 mb-3"></i>
            <h2 className="fw-bold">Access Denied</h2>
            <p className="text-muted">Please login to access your library dashboard.</p>
            <Link to="/login" className="btn btn-primary-custom mt-3">Login Now</Link>
         </div>
      </div>
    );
  }

  const isAdmin = role.toUpperCase() === "ADMIN";

  return (
    <div className="container py-5">
      <div className="mb-5">
        <h2 className="fw-bold mb-1">Dashboard Overview</h2>
        <p className="opacity-75">
          Hello, <span className="fw-bold text-white">{name}</span>! {isAdmin ? "Welcome to Admin's Dashboard." : "Welcome to Student's Dashboard."}
        </p>
      </div>
 
      <div className="row g-4">
 
        <div className="col-md-6 mb-4">
          <div className="card glass-card p-5 border-0 h-100 text-center" style={{ cursor: "pointer" }} onClick={() => navigate('/books')}>
            <h1 style={{ fontSize: "3.5rem" }} className="text-primary mb-3"><i className="bi bi-book"></i></h1>
            <h4 className="fw-bold mt-2">{isAdmin ? "Manage Books" : "View Books"}</h4>
            <p className="opacity-75 small">{isAdmin ? "View, add, update, or remove books" : "Browse and reserve books"}</p>
          </div>
        </div>
 
        <div className="col-md-6 mb-4">
          <div className="card glass-card p-5 border-0 h-100 text-center" style={{ cursor: "pointer" }} onClick={() => navigate('/categories')}>
            <h1 style={{ fontSize: "3.5rem" }} className="text-primary mb-3"><i className="bi bi-tags"></i></h1>
            <h4 className="fw-bold mt-2">{isAdmin ? "Manage Categories" : "View Categories"}</h4>
            <p className="opacity-75 small">{isAdmin ? "Organize books into sections" : "Browse books by category"}</p>
          </div>
        </div>
 
        {isAdmin && (
          <>
            <div className="col-md-6 mb-4">
              <div className="card glass-card p-5 border-0 h-100 text-center" style={{ cursor: "pointer" }} onClick={() => navigate('/admin/borrow-history')}>
                <h1 style={{ fontSize: "3.5rem" }} className="text-primary mb-3"><i className="bi bi-person-lines-fill"></i></h1>
                <h4 className="fw-bold mt-2">All Borrowings</h4>
                <p className="opacity-75 small">View all students borrow records & fines</p>
              </div>
            </div>
            <div className="col-md-6 mb-4">
              <div className="card glass-card p-5 border-0 h-100 text-center" style={{ cursor: "pointer" }} onClick={() => navigate('/admin/reservations')}>
                <h1 style={{ fontSize: "3.5rem" }} className="text-primary mb-3"><i className="bi bi-journal-bookmark"></i></h1>
                <h4 className="fw-bold mt-2">All Reservations</h4>
                <p className="opacity-75 small">Monitor all active book reservations</p>
              </div>
            </div>
          </>
        )}
 
        {!isAdmin && (
          <>
            <div className="col-md-6 mb-4">
              <div className="card glass-card p-5 border-0 h-100 text-center" style={{ cursor: "pointer" }} onClick={() => navigate('/history')}>
                <h1 style={{ fontSize: "3.5rem" }} className="text-primary mb-3"><i className="bi bi-clock-history"></i></h1>
                <h4 className="fw-bold mt-2">My History</h4>
                <p className="opacity-75 small">View your borrowed books and fines</p>
              </div>
            </div>
            <div className="col-md-6 mb-4">
              <div className="card glass-card p-5 border-0 h-100 text-center" style={{ cursor: "pointer" }} onClick={() => navigate('/reservations')}>
                <h1 style={{ fontSize: "3.5rem" }} className="text-primary mb-3"><i className="bi bi-calendar-check"></i></h1>
                <h4 className="fw-bold mt-2">My Reservations</h4>
                <p className="opacity-75 small">View your active book reservations</p>
              </div>
            </div>
          </>
        )}

      </div>
    </div>
  );
}

export default Dashboard;