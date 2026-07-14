import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "16px 6vw",
        backgroundColor: "var(--color-secondary)",
        borderBottom: "1px solid var(--color-border)",
        position: "sticky",
        top: 0,
        zIndex: 10,
      }}
    >
      <Link to="/" style={{ display: "flex", alignItems: "center", gap: "8px" }}>
        <span style={{ fontSize: "1.5rem" }}>🎬</span>
        <span style={{ fontWeight: 700, fontSize: "1.25rem", color: "var(--color-primary)" }}>
          CineRate
        </span>
      </Link>

      <div style={{ display: "flex", alignItems: "center", gap: "24px" }}>
        <Link to="/">Browse</Link>
        {user && <Link to="/recommendations">For You</Link>}
        {user && <Link to="/watchlist">Watchlist</Link>}
        {user?.role === "ADMIN" && <Link to="/admin">Admin</Link>}

        {user ? (
          <>
            <span style={{ color: "var(--color-muted)" }}>Hi, {user.username}</span>
            <button
              onClick={handleLogout}
              style={{
                background: "transparent",
                border: "1px solid var(--color-primary)",
                color: "var(--color-primary)",
                borderRadius: "var(--radius-sm)",
                padding: "6px 14px",
              }}
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link
              to="/register"
              style={{
                background: "var(--color-primary)",
                color: "#0F172A",
                fontWeight: 600,
                borderRadius: "var(--radius-sm)",
                padding: "8px 16px",
              }}
            >
              Sign Up
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}
