import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Categories() {
  const [categories, setCategories] = useState([]);
  const navigate = useNavigate();
  const role = localStorage.getItem("role") || "";
  const isAdmin = role.toUpperCase() === "ADMIN";

  useEffect(() => {
    fetchCategories();
  }, []);

  const getAuthHeaders = () => {
    const token = localStorage.getItem("token");
    return {
      "Content-Type": "application/json",
      "Authorization": token ? `Bearer ${token}` : ""
    };
  };

  const checkAuthError = (res) => {
    if (res.status === 401 || res.status === 403) {
      alert("Your session has expired or you are not authorized. Please log in again.");
      localStorage.removeItem("token");
      navigate("/login");
      return true;
    }
    return false;
  };

  const fetchCategories = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/categories/getallcategories", {
        headers: getAuthHeaders()
      });
      if (res.ok) {
        const data = await res.json();
        setCategories(data || []);
      } else {
        checkAuthError(res);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Delete this category?")) {
      try {
        const res = await fetch(`http://localhost:8080/api/categories/deletecategory/${id}`, {
          method: "DELETE",
          headers: getAuthHeaders()
        });
        if (res.ok) {
          alert("Category Deleted Successfully");
          fetchCategories();
        } else {
          if (!checkAuthError(res)) {
            alert("Delete failed. It may be associated with existing books.");
          }
        }
      } catch (err) {
        alert("Delete failed.");
      }
    }
  }

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <h2 style={{ color: "#1e3c72", fontWeight: "700" }} className="mb-4">{isAdmin ? "Manage Categories" : "Explore Categories"}</h2>

          <div className="card glass-card p-0 overflow-hidden">
            <ul className="list-group list-group-flush border-0">
              {categories.map(c => (
                <li key={c.categoryId} className="list-group-item d-flex justify-content-between align-items-center py-3 border-bottom">
                  <span className="fw-semibold text-dark">{c.name}</span>
                  <div className="d-flex align-items-center">
                    <button className="btn btn-sm btn-primary-custom me-2" onClick={() => navigate(`/books?categoryName=${encodeURIComponent(c.name)}`)}>
                      View Books
                    </button>
                    {isAdmin && (
                      <button className="btn btn-sm btn-outline-danger border-0 shadow-sm" onClick={() => handleDelete(c.categoryId)}>
                        <i className="bi bi-trash"></i> Delete
                      </button>
                    )}
                  </div>
                </li>
              ))}
              {categories.length === 0 && <li className="list-group-item text-center py-4 text-muted">No Categories Found</li>}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Categories;
