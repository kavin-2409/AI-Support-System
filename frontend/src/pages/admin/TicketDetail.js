import React, { useEffect, useState, useCallback } from "react";
import API from "../../services/api";
import { useParams, useNavigate } from "react-router-dom";
import "./admin.css"; // FIXED: Changed to lowercase

function TicketDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [ticket, setTicket] = useState(null);

  // FIXED: Wrapping in useCallback tracks the function safely for useEffect
  const fetchTicket = useCallback(async () => {
    try {
      const res = await API.get(`/admin/tickets`);
      // FIXED: Safely converted both to Strings to use '===' strict equality
      const found = res.data.find((t) => String(t.id) === String(id));
      setTicket(found);
    } catch (err) {
      console.error("Failed to fetch ticket details", err);
    }
  }, [id]);

  useEffect(() => {
    fetchTicket();
  }, [fetchTicket]); // fetchTicket is now safely in the dependency array

  const updateStatus = async (status) => {
    try {
      await API.put(`/admin/tickets/${id}`, { status });
      fetchTicket();
    } catch (err) {
      console.error("Failed to update status", err);
    }
  };

  if (!ticket) return (
    <div className="admin-layout" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
      <p style={{ color: '#8b949e', fontSize: '1.2rem' }}>Loading ticket details...</p>
    </div>
  );

  return (
    <div className="admin-layout">
      <div className="detail-card">

        <button className="back-btn" onClick={() => navigate(-1)}>
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="19" y1="12" x2="5" y2="12"></line><polyline points="12 19 5 12 12 5"></polyline></svg>
          Back to Tickets
        </button>

        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h2>Ticket #{ticket.id}</h2>
          <span className={`status-badge status-${ticket.status}`}>
            {ticket.status.replace('_', ' ')}
          </span>
        </div>

        <div className="detail-query-box">
          <h3>User Query</h3>
          <p>"{ticket.query}"</p>
        </div>

        <div className="actions-section">
          <h3>Update Ticket Status</h3>
          <div className="button-group">
            <button className="action-btn btn-open" onClick={() => updateStatus("OPEN")}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>
              Mark as Open
            </button>

            <button className="action-btn btn-resolved" onClick={() => updateStatus("RESOLVED")}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
              Mark as Resolved
            </button>

            <button className="action-btn btn-human" onClick={() => updateStatus("NEEDS_HUMAN")}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
              Escalate to Human
            </button>
          </div>
        </div>

      </div>
    </div>
  );
}

export default TicketDetail;