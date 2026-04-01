
import { Link } from "react-router-dom";

function Home() {
  const token = localStorage.getItem("token");

  return (
    <div className="container py-5">
      <div className="row justify-content-center text-center py-5">
        <div className="col-lg-10">
          <div className="hero-badge rounded-pill shadow-sm">
            Modern Library Management System - Where Reserving, Borrowing, Viewing and Cataloging became easier
          </div>
          <h1 className="display-2 fw-bold mb-4" style={{ lineHeight: "1.1" }}>
            The smarter way to <span style={{ color: "#60a5fa" }}>Manage Your Library</span>
          </h1>
          <p className="lead opacity-75 fw-bold mb-5 px-md-5 fs-2">
            A minimalist, powerful workspace for managing books, students, and circulation records with ease and precision.
          </p>

          <div className="d-flex gap-3 justify-content-center mb-5">
            {!token ? (
              <>
                <Link to="/register" className="btn btn-primary-custom px-5 py-3 shadow-sm border-0" style={{ background: '#3b82f6' }}>
                  Get Started
                </Link>
                <Link to="/login" className="btn btn-outline-light px-5 py-3 shadow-sm border-2">
                  Sign In
                </Link>
              </>
            ) : (
              <Link to="/dashboard" className="btn btn-primary-custom px-5 py-3 shadow-sm border-0" style={{ background: '#3b82f6' }}>
                Go to Dashboard
              </Link>
            )}
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="row g-4 mt-5 pb-5">
        <div className="col-md-4">
          <div className="card h-100 p-4 shadow-lg border-0">
            <div className="card-body">
              <div className="mb-4 text-primary"><i className="bi bi-journal-text fs-1"></i></div>
              <h4 className="fw-bold mb-3">Cataloging</h4>
              <p className="small opacity-75">Precision tools for managing book inventory, ISBNs, and specialized categories.</p>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card h-100 p-4 shadow-lg border-0">
            <div className="card-body">
              <div className="mb-4 text-primary"><i className="bi bi-arrow-repeat fs-1"></i></div>
              <h4 className="fw-bold mb-3">Circulation</h4>
              <p className="small opacity-75">Seamless borrow and return workflows with automated fine calculations.</p>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card h-100 p-4 shadow-lg border-0">
            <div className="card-body">
              <div className="mb-4 text-primary"><i className="bi bi-shield-check fs-1"></i></div>
              <h4 className="fw-bold mb-3">Advanced Security</h4>
              <p className="small opacity-75">Role-based access control protecting student privacy and admin operations.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Home;