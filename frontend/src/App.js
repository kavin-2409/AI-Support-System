import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import TicketList from "./pages/admin/TicketList";
import TicketDetail from "./pages/admin/TicketDetail";

function App() {
return (
<BrowserRouter>
<Routes>

    {/* AUTH ROUTES */}
    <Route path="/" element={<Login />} />
    <Route path="/register" element={<Register />} />

    {/* USER ROUTE */}
    <Route path="/dashboard" element={<Dashboard />} />

    {/* ADMIN ROUTES */}
    <Route path="/admin" element={<TicketList />} />
    <Route path="/admin/ticket/:id" element={<TicketDetail />} />

  </Routes>
</BrowserRouter>

);
}

export default App;