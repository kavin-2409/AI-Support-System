import React, { useEffect, useState } from "react";
import API from "../../services/api";
import { useNavigate } from "react-router-dom";
import "./admin.css"; // FIXED: Changed to lowercase to match your file exactly

function TicketList() {
  const [tickets, setTickets] = useState([]);
  const navigate = useNavigate();
  if (localStorage.getItem("role") !== "ADMIN") {
    window.location.href = "/";
  }

  useEffect(() => {
    // FIXED: Moving the fetch function inside useEffect satisfies the ESLint warning
    const fetchTickets = async () => {
      try {
        const res = await API.get("/admin/tickets");
        setTickets(res.data);
      } catch (err) {
        console.error("Failed to fetch tickets", err);
      }
    };

    fetchTickets();
  }, []);

  return (
    <div className="admin-layout">
      <div className="admin-header">
        <h2>Admin Dashboard</h2>
        <span style={{ color: "#8b949e" }}>Total Tickets: {tickets.length}</span>
      </div>

      <div className="ticket-grid">
        {tickets.map((ticket) => (
          <div
            key={ticket.id}
            className="ticket-card"
            onClick={() => navigate(`/admin/ticket/${ticket.id}`)}
          >
            <p className="ticket-query">"{ticket.query}"</p>

            <div className="ticket-meta">
              <span className={`status-badge status-${ticket.status}`}>
                {ticket.status.replace('_', ' ')}
              </span>
              <span style={{ display: "flex", alignItems: "center", gap: "5px" }}>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>
               {new Date(ticket.createdAt).toLocaleString()}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default TicketList;