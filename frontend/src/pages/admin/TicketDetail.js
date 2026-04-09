import React, { useEffect, useState } from "react";
import API from "../../services/api";
import { useParams, useNavigate } from "react-router-dom";
import "./admin.css";

function TicketDetail() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [ticket, setTicket] = useState(null);
  const [messages, setMessages] = useState([]);

  // 🔥 FETCH TICKET + MESSAGES
  useEffect(() => {
    const fetchTicket = async () => {
      try {
        // 🔥 TEMP FIX: direct backend call
        const res = await API.get(`http://localhost:8080/tickets/${id}`);

        setTicket(res.data.ticket);
        setMessages(res.data.messages);
      } catch (err) {
        console.error("Error fetching ticket:", err);
      }
    };

    fetchTicket();
  }, [id]);

  // 🔥 UPDATE STATUS
  const updateStatus = async (status) => {
    try {
      await API.put(`http://localhost:8080/admin/tickets/${id}`, { status });
      window.location.reload(); // simple refresh
    } catch (err) {
      console.error("Failed to update status", err);
    }
  };

  // 🔥 LOADING STATE
  if (!ticket) {
    return (
      <div className="admin-layout" style={{ display: "flex", justifyContent: "center", alignItems: "center" }}>
        <p style={{ color: "#8b949e", fontSize: "1.2rem" }}>
          Loading ticket details...
        </p>
      </div>
    );
  }

  return (
    <div className="admin-layout">
      <div className="detail-card">

        {/* 🔙 BACK BUTTON */}
        <button className="back-btn" onClick={() => navigate(-1)}>
          ← Back to Tickets
        </button>

        {/* 🎯 HEADER */}
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <h2>Ticket #{ticket.id}</h2>
          <span className={`status-badge status-${ticket.status}`}>
            {ticket.status.replace("_", " ")}
          </span>
        </div>

        {/* 📝 QUERY */}
        <div className="detail-query-box">
          <h3>User Query</h3>
          <p>"{ticket.query}"</p>
        </div>

        {/* 💬 CONVERSATION */}
        <div className="detail-query-box">
          <h3>Conversation</h3>

          {messages.length === 0 ? (
            <p style={{ color: "#8b949e" }}>No messages found</p>
          ) : (
            messages.map((msg) => (
              <div
                key={msg.id}
                style={{
                  marginBottom: "10px",
                  padding: "10px",
                  borderRadius: "6px",
                  backgroundColor: msg.sender === "USER" ? "#1f6feb" : "#30363d",
                  color: "white",
                }}
              >
                <strong>{msg.sender}:</strong> {msg.message}
              </div>
            ))
          )}
        </div>

        {/* ⚙️ ACTIONS */}
        <div className="actions-section">
          <h3>Update Ticket Status</h3>

          <div className="button-group">
            <button className="action-btn btn-open" onClick={() => updateStatus("OPEN")}>
              Mark as Open
            </button>

            <button className="action-btn btn-resolved" onClick={() => updateStatus("RESOLVED")}>
              Mark as Resolved
            </button>

            <button className="action-btn btn-human" onClick={() => updateStatus("NEEDS_HUMAN")}>
              Escalate to Human
            </button>
          </div>
        </div>

      </div>
    </div>
  );
}

export default TicketDetail;