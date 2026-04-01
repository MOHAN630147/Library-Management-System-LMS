import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

function Register() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    phoneNumber: "",
    role: ""
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/api/users/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
      });

      const data = await response.json();

      if (response.ok && data.message === "User Registered Successfully") {
        setSuccess("Registration successful! Redirecting to login...");
        setTimeout(() => navigate("/login"), 2000);
      } else {
        setError(data.message || "Registration failed");
      }
    } catch (err) {
      setError("An error occurred during registration. Make sure backend is running.");
      console.error(err);
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-card">
        <div className="text-center mb-4">
          <h2 className="fw-bold mb-1">Create Account</h2>
          <p className="text-muted small">Join our modern library community</p>
        </div>

        {error && <div className="alert alert-danger py-2 small">{error}</div>}
        {success && <div className="alert alert-success py-2 small">{success}</div>}

        <form onSubmit={handleRegister}>
          <div className="mb-3">
            <label className="form-label small fw-bold text-muted">Full Name</label>
            <input
              name="name"
              className="form-control"
              placeholder="Your Name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label small fw-bold text-muted">Email Address</label>
            <input
              type="email"
              name="email"
              className="form-control"
              placeholder="name@example.com"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label small fw-bold text-muted">Password</label>
            <input
              type="password"
              name="password"
              className="form-control"
              placeholder="Create a password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label small fw-bold text-muted">Phone Number</label>
            <input
              name="phoneNumber"
              className="form-control"
              placeholder="Contact Number"
              value={formData.phoneNumber}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-4">
            <label className="form-label small fw-bold text-muted">Account Type</label>
            <select className="form-select" name="role" value={formData.role} onChange={handleChange} required>
              <option value="" disabled>Select Role</option>
              <option value="Student">Student</option>
              <option value="Admin">Admin</option>
            </select>
          </div>

          <button type="submit" className="btn btn-primary-custom w-100 py-2">Create Account</button>
        </form>

        <div className="text-center mt-4 pt-3 border-top">
          <span className="text-muted small">Already have an account?</span>
          <Link to="/login" className="ms-2 small fw-bold text-primary text-decoration-none">Login here</Link>
        </div>
      </div>
    </div>
  );
}

export default Register;