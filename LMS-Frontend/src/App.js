import React from "react";
import { Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Books from "./pages/Books";
import Categories from "./pages/Categories";
import BorrowHistory from "./pages/BorrowHistory";
import Reservations from "./pages/Reservations";
import AdminBorrowHistory from "./pages/AdminBorrowHistory";
import AdminReservations from "./pages/AdminReservations";

function App() {
  return (
    <>
      <Navbar />
      <div className="app-content-wrapper">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/books" element={<Books />} />
          <Route path="/categories" element={<Categories />} />
          <Route path="/history" element={<BorrowHistory />} />
          <Route path="/reservations" element={<Reservations />} />
          <Route path="/admin/borrow-history" element={<AdminBorrowHistory />} />
          <Route path="/admin/reservations" element={<AdminReservations />} />
        </Routes>
      </div>
    </>
  );
}

export default App;