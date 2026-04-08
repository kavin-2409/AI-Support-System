import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Register from "./pages/Register";
import TicketList from "./pages/admin/TicketList";
import TicketDetail from "./pages/admin/TicketDetail";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/register" element={<Register />} />

        {/* ADMIN ROUTES */}
        <Route path="/admin" element={<TicketList />} />
        <Route path="/admin/ticket/:id" element={<TicketDetail />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;