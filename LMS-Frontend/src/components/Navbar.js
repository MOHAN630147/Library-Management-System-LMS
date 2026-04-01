import React from "react";
import { Link, useNavigate } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
    localStorage.removeItem("name");
    navigate("/login");
  };

  const role = localStorage.getItem("role") || "";
  const name = localStorage.getItem("name") || "";
  const isAdmin = role.toUpperCase() === "ADMIN";

  return (
    <nav className="navbar navbar-expand-lg navbar-custom sticky-top">
      <div className="container">
        <Link className="navbar-brand d-flex align-items-center gap-2" to="/">
          <div className="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center" style={{ width: '25px', height: '25px' }}>
            <i className="bi bi-book-half"></i>
          </div>
          <span className="fw-bold tracking-tight">Modern Library Management System</span>
        </Link>

        {token && (
          <div className="welcome-section d-none d-lg-flex align-items-center">
            <div className="text-end">
              <div className="text-white-50 small" style={{ fontSize: '0.7rem', marginBottom: '-2px' }}>Welcome back,</div>
              <div className="fw-bold text-white mb-0" style={{ fontSize: '0.95rem' }}>{name}</div>
            </div>
            <span className="ms-3 badge rounded-pill role-badge">
              {role}
            </span>
          </div>
        )}

        <button
          className="navbar-toggler border-0 shadow-none"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#nav"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="nav">
          <ul className="navbar-nav ms-auto align-items-center gap-lg-2">
            <li className="nav-item">
              <Link className="nav-link" to="/">Home</Link>
            </li>

            {!token ? (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/login">Sign In</Link>
                </li>
                <li className="nav-item ms-lg-2">
                  <Link className="btn btn-primary-custom" to="/register">Join Now</Link>
                </li>
              </>
            ) : (
              <>
                <li className="nav-item">
                  <Link className="nav-link px-3" to="/dashboard">
                    <i className="bi bi-speedometer2 me-2"></i>Dashboard
                  </Link>
                </li>
                <li className="nav-item ms-lg-3">
                  <button 
                    onClick={handleLogout} 
                    className="btn btn-outline-danger btn-sm px-3 rounded-pill fw-bold"
                    style={{ border: '1.5px solid rgba(220, 53, 69, 0.5)', transition: 'all 0.3s' }}
                  >
                    <i className="bi bi-box-arrow-right me-2"></i>Logout
                  </button>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;