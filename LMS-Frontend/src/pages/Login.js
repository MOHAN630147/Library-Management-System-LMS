import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/api/users/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ email, password })
      });
      
      const data = await response.json();
      
      if (response.ok && data.token) {
        localStorage.setItem("token", data.token);
        localStorage.setItem("role", data.role);
        localStorage.setItem("userId", data.userId);
        localStorage.setItem("name", data.name);
        navigate("/dashboard");
      } else {
        setError(data.message || "Invalid Credentials");
      }
    } catch (err) {
      setError("An error occurred during login. Please ensure the backend is running.");
      console.error(err);
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-card">
        <div className="text-center mb-4">
          <h2 className="fw-bold mb-1">Welcome Back</h2>
          <p className="text-muted small">Access your library dashboard</p>
        </div>
        
        {error && <div className="alert alert-danger py-2 small">{error}</div>}
        
        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <label className="form-label small fw-bold text-muted">Email Address</label>
            <input 
              type="email" 
              className="form-control" 
              placeholder="name@example.com" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="mb-4">
            <label className="form-label small fw-bold text-muted">Password</label>
            <input 
              type="password" 
              className="form-control" 
              placeholder="••••••••" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary-custom w-100 py-2">Sign In</button>
        </form>
        
        <div className="text-center mt-4 pt-3 border-top">
          <span className="text-muted small">Don't have an account?</span>
          <Link to="/register" className="ms-2 small fw-bold text-primary text-decoration-none">Register Now</Link>
        </div>
      </div>
    </div>
  );
}

export default Login;