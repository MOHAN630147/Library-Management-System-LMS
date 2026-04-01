import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

function Books() {
  const navigate = useNavigate();
  const location = useLocation();
  const [books, setBooks] = useState([]);
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [categories, setCategories] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [selectedBook, setSelectedBook] = useState(null);
  
  const role = localStorage.getItem("role") || "";
  const userId = localStorage.getItem("userId");
  const isAdmin = role.toUpperCase() === "ADMIN";

  const [formData, setFormData] = useState({
    title: "", author: "", description: "", isbn: "",
    publisher: "", language: "", availableQuantity: 1,
    pageCount: "", bookPrice: "", categoryName: "",
    edition: "", publicationDate: ""
  });

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

  useEffect(() => {
    fetchBooks();
    fetchCategories();
  }, []);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const catName = params.get("categoryName");
    if (catName && books.length > 0) {
      setFilteredBooks(books.filter(b => b.category?.name === catName));
    } else {
      setFilteredBooks(books);
    }
  }, [location.search, books]);

  const fetchBooks = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/books/getallbooks", {
        headers: getAuthHeaders()
      });
      if (res.ok) {
        const data = await res.json();
        setBooks(data || []);
      } else {
        checkAuthError(res);
      }
    } catch (err) {
      // Fetch error handled
    }
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
      // Fetch error handled
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (!formData.categoryName) return alert("Enter Category");

      let selectedCategoryId = null;
      let currentCategories = [...categories];
      const normalizedInput = formData.categoryName.trim().toLowerCase();
      const existingCategory = currentCategories.find(c => c.name && c.name.trim().toLowerCase() === normalizedInput);

      if (existingCategory) {
        selectedCategoryId = existingCategory.categoryId;
      } else {
        const catRes = await fetch("http://localhost:8080/api/categories/addcategory", {
          method: "POST",
          headers: getAuthHeaders(),
          body: JSON.stringify({ name: formData.categoryName })
        });

        if (catRes.ok) {
          const fetchRes = await fetch("http://localhost:8080/api/categories/getallcategories", {
            headers: getAuthHeaders()
          });
          if (fetchRes.ok) {
            currentCategories = await fetchRes.json();
            setCategories(currentCategories || []);
            const newCat = (currentCategories || []).find(c => c.name && c.name.trim().toLowerCase() === normalizedInput);
            if (newCat) {
              selectedCategoryId = newCat.categoryId;
            } else {
              return alert("Failed to retrieve new category ID");
            }
          } else {
            return alert("Failed to fetch updated categories");
          }
        } else {
          return alert("Error adding new category");
        }
      }

      const payload = { ...formData };
      payload.availableQuantity = parseInt(payload.availableQuantity) || 1;
      payload.pageCount = payload.pageCount ? parseInt(payload.pageCount) : 0;
      payload.bookPrice = payload.bookPrice ? parseFloat(payload.bookPrice) : 0.0;
      payload.categoryId = selectedCategoryId;
      delete payload.categoryName;
      if (!payload.publicationDate) payload.publicationDate = null;

      const url = selectedBook && showModal && !showDetailsModal
        ? `http://localhost:8080/api/books/updatebook`
        : "http://localhost:8080/api/books/addbook";
      
      const method = selectedBook && showModal && !showDetailsModal ? "PUT" : "POST";

      const res = await fetch(url, {
        method: method,
        headers: getAuthHeaders(),
        body: JSON.stringify(payload)
      });

      if (res.ok) {
        setShowModal(false);
        setSelectedBook(null);
        fetchBooks();
        setFormData({
          title: "", author: "", description: "", isbn: "",
          publisher: "", language: "", availableQuantity: 1,
          pageCount: "", bookPrice: "", categoryName: "",
          edition: "", publicationDate: ""
        });
      } else {
        if (!checkAuthError(res)) {
          const errData = await res.text();
          alert("Error saving book: " + errData);
        }
      }
    } catch (err) {
      alert("Network Error while saving book");
    }
  };

  const handleReserve = async (bookId) => {
    try {
      const res = await fetch(`http://localhost:8080/api/reservations/reserve?userId=${userId}&bookId=${bookId}`, {
        method: "POST",
        headers: getAuthHeaders()
      });
      if (res.ok) {
        alert("Book Reserved Successfully");
      } else {
        const data = await res.json();
        alert(data.message || "Reservation Failed (Limit reached or book unavailable)");
      }
    } catch (err) {
      alert("Error reserving book");
    }
  };

  const handleBorrow = async (bookId) => {
    try {
      const res = await fetch(`http://localhost:8080/api/borrow/borrowbook?userId=${userId}&bookId=${bookId}`, {
        method: "POST",
        headers: getAuthHeaders()
      });
      if (res.ok) {
        alert("Book Borrowed Successfully");
        fetchBooks();
      } else {
        const data = await res.json();
        alert(data.message || "Borrowing Failed (Limit reached or book unavailable)");
      }
    } catch (err) {
      alert("Error borrowing book");
    }
  };

  const openDetails = (book) => {
    setSelectedBook(book);
    setShowDetailsModal(true);
  };

  const handleEdit = (book) => {
    setSelectedBook(book);
    setFormData({
      bookId: book.bookId,
      title: book.title,
      author: book.author,
      description: book.description || "",
      isbn: book.isbn,
      publisher: book.publisher || "",
      language: book.language || "",
      availableQuantity: book.availableQuantity,
      pageCount: book.pageCount || "",
      bookPrice: book.bookPrice || "",
      categoryName: book.category?.name || "",
      edition: book.edition || "",
      publicationDate: book.publicationDate || ""
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this book?")) {
      try {
        const res = await fetch(`http://localhost:8080/api/books/deletebook/${id}`, {
          method: "DELETE",
          headers: getAuthHeaders()
        });
        if (res.ok) {
          alert("Book Deleted Successfully");
          fetchBooks();
        } else {
          if (!checkAuthError(res)) {
            alert("Error deleting book");
          }
        }
      } catch (err) {
        alert("Error deleting book");
      }
    }
  }

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 style={{ color: "#1e3c72", fontWeight: "700" }}>{isAdmin ? "Manage Books" : "Library Catalog"}</h2>
          {new URLSearchParams(location.search).get("categoryName") && (
            <button className="btn btn-sm btn-outline-secondary mt-1 fw-bold" onClick={() => navigate('/categories')}>
              &larr; Back to Categories
            </button>
          )}
        </div>
        {isAdmin && (
          <button className="btn btn-primary-custom" onClick={() => setShowModal(true)}>
            + Add New Book
          </button>
        )}
      </div>

      <div className="card p-4 shadow-lg border-0" style={{ backgroundColor: "#fdf5e6", borderRadius: "15px" }}>
        {filteredBooks.length === 0 ? (
          <p className="text-center text-muted my-4">No books found in the library.</p>
        ) : (
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-dark">
                <tr className="small text-uppercase fw-bold">
                  <th className="py-3 px-3">Book Name</th>
                  <th className="py-3">Category</th>
                  <th className="py-3">Stock</th>
                  <th className="py-3 px-3 text-end">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredBooks.map(b => (
                  <tr key={b.bookId}>
                    <td style={{ color: "#000000" }}>
                      <div className="fw-bold" style={{ fontSize: "1.1rem" }}>{b.title}</div>
                      <div style={{ opacity: 0.8 }}>by {b.author}</div>
                    </td>
                    <td style={{ color: "#000000" }}>
                      <span className="badge bg-secondary">{b.category?.name || "Uncategorized"}</span>
                    </td>
                    <td style={{ color: "#000000" }}>
                      <span className={`fw-bold ${b.availableQuantity > 0 ? 'text-success' : 'text-danger'}`}>
                        {b.availableQuantity} available
                      </span>
                    </td>
                    <td className="text-end">
                      <div className="d-flex justify-content-end gap-2">
                        <button className="btn btn-sm btn-outline-info" onClick={() => openDetails(b)}>Details</button>
                        {isAdmin ? (
                          <>
                            <button className="btn btn-sm btn-outline-primary" onClick={() => handleEdit(b)}>Edit</button>
                            <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(b.bookId)}>Delete</button>
                          </>
                        ) : (
                          <>
                            <button className="btn btn-sm btn-success" onClick={() => handleBorrow(b.bookId)} disabled={b.availableQuantity === 0}>
                              Borrow
                            </button>
                            <button className="btn btn-sm btn-primary-custom" onClick={() => handleReserve(b.bookId)} disabled={b.availableQuantity === 0}>
                              Reserve
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {showModal && (
        <div className="modal d-block" style={{ backgroundColor: "rgba(0,0,0,0.8)", zIndex: 1100, backdropFilter: "blur(5px)" }}>
          <div className="modal-dialog modal-lg modal-dialog-centered">
            <div className="modal-content glass-card border-0 shadow-lg">
              <div className="modal-header border-0">
                <h5 className="modal-title fw-bold text-white">{selectedBook ? "Edit Book" : "Add New Book"}</h5>
                <button type="button" className="btn-close btn-close-white" onClick={() => { setShowModal(false); setSelectedBook(null); }}></button>
              </div>
              <div className="modal-body p-4 text-white">
                <form onSubmit={handleSubmit}>
                  <div className="row g-3">
                    <div className="col-md-6">
                      <input name="title" className="form-control" placeholder="Book Title" value={formData.title} onChange={handleChange} required />
                    </div>
                    <div className="col-md-6">
                      <input name="author" className="form-control" placeholder="Author Name" value={formData.author} onChange={handleChange} required />
                    </div>
                    <div className="col-md-4">
                      <input name="isbn" className="form-control" placeholder="ISBN" value={formData.isbn} onChange={handleChange} required />
                    </div>
                    <div className="col-md-4">
                      <input name="publisher" className="form-control" placeholder="Publisher" value={formData.publisher} onChange={handleChange} />
                    </div>
                    <div className="col-md-4">
                      <input name="edition" className="form-control" placeholder="Edition" value={formData.edition} onChange={handleChange} />
                    </div>

                    <div className="col-md-3">
                      <input name="language" className="form-control" placeholder="Language" value={formData.language} onChange={handleChange} />
                    </div>
                    <div className="col-md-3">
                      <input type="number" min="1" name="availableQuantity" className="form-control" placeholder="Quantity" value={formData.availableQuantity} onChange={handleChange} required />
                    </div>
                    <div className="col-md-3">
                      <input type="number" name="pageCount" className="form-control" placeholder="Pages" value={formData.pageCount} onChange={handleChange} />
                    </div>
                    <div className="col-md-3">
                      <input type="number" step="0.01" name="bookPrice" className="form-control" placeholder="Price ($)" value={formData.bookPrice} onChange={handleChange} />
                    </div>

                    <div className="col-md-6">
                      <input
                        name="categoryName"
                        className="form-control"
                        placeholder="Category Name"
                        value={formData.categoryName}
                        onChange={handleChange}
                        list="categoryOptions"
                        required
                      />
                      <datalist id="categoryOptions">
                        {categories.map(c => <option key={c.categoryId} value={c.name} />)}
                      </datalist>
                    </div>
                    <div className="col-md-6">
                      <input type="date" name="publicationDate" className="form-control" title="Publication Date" value={formData.publicationDate} onChange={handleChange} />
                    </div>

                    <div className="col-12">
                      <textarea name="description" className="form-control" placeholder="Book Description" value={formData.description} onChange={handleChange} rows="3"></textarea>
                    </div>
                  </div>
                  <div className="text-end mt-4">
                    <button type="button" className="btn btn-outline-light me-2 px-4 border-0" onClick={() => setShowModal(false)}>Cancel</button>
                    <button type="submit" className="btn btn-primary-custom px-4">Save Book</button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      )}
      
      {showDetailsModal && (
        <DetailsModal 
          book={selectedBook} 
          onClose={() => setShowDetailsModal(false)} 
          onBorrow={handleBorrow}
          onReserve={handleReserve}
          isAdmin={isAdmin}
        />
      )}
    </div>
  );
}

const DetailsModal = ({ book, onClose, onBorrow, onReserve, isAdmin }) => {
  if (!book) return null;
  
  return (
    <div className="modal d-block" style={{ backgroundColor: "rgba(0,0,0,0.8)", zIndex: 1100, backdropFilter: "blur(5px)" }}>
      <div className="modal-dialog modal-lg modal-dialog-centered">
        <div className="modal-content glass-card border-0 shadow-lg" style={{ borderRadius: "20px" }}>
          <div className="modal-header border-0 pb-0 pt-4 px-4">
            <h4 className="modal-title fw-bold text-white">Book Details</h4>
            <button type="button" className="btn-close btn-close-white" onClick={onClose}></button>
          </div>
          <div className="modal-body p-4 text-white">
            <div className="mb-4 text-center">
              <h2 className="fw-bold mb-1" style={{ fontSize: "2.5rem" }}>{book.title}</h2>
              <h4 className="text-white-50">by {book.author}</h4>
              <hr className="my-4 border-white opacity-25" />
            </div>

            <div className="row g-4 justify-content-center">
               <div className="col-md-10">
                  <div className="mb-4">
                    <p className="text-white-50 small text-uppercase fw-bold mb-2">Description</p>
                    <p className="fs-5" style={{ lineHeight: "1.8" }}>
                      {book.description || "Detailed description is coming soon for this book."}
                    </p>
                  </div>

                  <div className="row g-3 p-3 rounded" style={{ backgroundColor: "rgba(255,255,255,0.05)" }}>
                    {[
                      { label: "Category", val: book.category?.name || "General" },
                      { label: "Publisher", val: book.publisher },
                      { label: "Edition", val: book.edition },
                      { label: "Language", val: book.language || "English" },
                      { label: "Published Date", val: book.publicationDate },
                      { label: "Price", val: `$${book.bookPrice || "0.00"}`, color: "text-success fw-bold" }
                    ].map((item, idx) => (
                      <div className="col-6 col-md-4" key={idx}>
                        <span className="text-white-50 d-block small text-uppercase">{item.label}</span>
                        <span className={item.color || "text-white fw-semibold"}>{item.val || "N/A"}</span>
                      </div>
                    ))}
                  </div>
               </div>
            </div>
          </div>
          <div className="modal-footer border-0 pt-0 px-4 pb-4">
            {!isAdmin ? (
              <div className="w-100 d-flex gap-2">
                <button className="btn btn-success flex-grow-1 fw-bold py-2" onClick={() => { onBorrow(book.bookId); onClose(); }} disabled={book.availableQuantity <= 0}>
                   Borrow Now
                </button>
                <button className="btn btn-primary-custom flex-grow-1 fw-bold py-2" onClick={() => { onReserve(book.bookId); onClose(); }} disabled={book.availableQuantity <= 0}>
                   Reserve for Later
                </button>
              </div>
            ) : (
              <button className="btn btn-secondary w-100 fw-bold py-2" onClick={onClose}>Close</button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Books;
