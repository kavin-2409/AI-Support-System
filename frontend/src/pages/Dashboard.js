import React, { useState, useRef, useEffect } from "react";
import API from "../services/api";
import "./Dashboard.css";

function Dashboard() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState([]);

  // UI ONLY: Reference to auto-scroll chat to the bottom
  const messagesEndRef = useRef(null);
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };
  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // YOUR EXACT LOGIC (Untouched)
  const handleSend = async () => {
    if (!input.trim()) return;

    const userMessage = {
      text: input,
      sender: "user",
    };

    // show user message immediately
    setMessages((prev) => [...prev, userMessage]);

    try {
      const res = await API.post("/tickets", {
        query: input,
      });

      const botMessage = {
        text: res.data,
        sender: "bot",
      };

      setMessages((prev) => [...prev, botMessage]);
    } catch (err) {
      console.error(err);

      setMessages((prev) => [
        ...prev,
        { text: "Error getting response", sender: "bot" },
      ]);
    }

    setInput("");
  };

  // UI ONLY: Allow pressing 'Enter' to send
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSend();
    }
  };

  return (
    <div className="dashboard-wrapper">
      <div className="desktop-layout">

        {/* --- LEFT SIDEBAR (TICKET HISTORY) --- */}
        <aside className="ticket-sidebar">
          <div className="sidebar-header">
            <h2>Support Portal</h2>
          </div>
          <div className="ticket-list">
            {/* The hardcoded "Active Session" item you requested to remove is gone from here. */}
            {/* You can map your real dynamic ticket history here in the future. */}
          </div>
        </aside>

        {/* --- RIGHT MAIN AREA (CHAT) --- */}
        <main className="chat-main">

          {/* HEADER */}
          <header className="chat-header">
            <div className="header-left">
              <div className="bot-avatar-container">
                <div className="badge">1</div>
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/>
                </svg>
                <div className="status-dot"></div>
              </div>
              <div className="header-info">
                <h3>MindX Bot</h3>
                <span>Online</span>
              </div>
            </div>
          </header>

          {/* CHAT BODY */}
          <div className="chat-body">
            {messages.length === 0 && (
               <div className="chat-date-divider">Start of conversation</div>
            )}

            {/* YOUR MESSAGES MAPPED TO NEW UI */}
            {messages.map((msg, index) => (
              <div key={index} className={`message-wrapper ${msg.sender}`}>
                {msg.sender === "bot" && (
                  <div className="sender-label">
                    <span className="sender-avatar-small"></span> MindX Bot
                  </div>
                )}
                {msg.sender === "user" && (
                  <div className="sender-label">You</div>
                )}
                <div className="message-bubble" style={{ whiteSpace: "pre-wrap" }}>
                  {msg.text}
                </div>
              </div>
            ))}
            {/* Invisible div for auto-scrolling */}
            <div ref={messagesEndRef} />
          </div>

          {/* INPUT AREA */}
          <div className="chat-input-area">
            <div className="input-wrapper">
              <input
                type="text"
                className="chat-input"
                placeholder="Type your message here..."
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyPress={handleKeyPress}
              />
              <button
                className={`send-btn ${input.trim() ? 'active' : ''}`}
                onClick={handleSend}
              >
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <line x1="22" y1="2" x2="11" y2="13"></line>
                  <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
                </svg>
              </button>
            </div>
          </div>
        </main>

      </div>
    </div>
  );
}

export default Dashboard;