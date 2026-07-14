import React from "react";
import { Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import MovieList from "./components/MovieList";
import MovieDetail from "./components/MovieDetail";
import Login from "./components/Login";
import Register from "./components/Register";
import Recommendations from "./components/Recommendations";
import Watchlist from "./components/Watchlist";
import AdminPanel from "./components/AdminPanel";

function App() {
  return (
    <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <Navbar />
      <main style={{ flex: 1, padding: "24px 6vw" }}>
        <Routes>
          <Route path="/" element={<MovieList />} />
          <Route path="/movies/:id" element={<MovieDetail />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/recommendations" element={<Recommendations />} />
          <Route path="/watchlist" element={<Watchlist />} />
          <Route path="/admin" element={<AdminPanel />} />
        </Routes>
      </main>
      <footer
        style={{
          textAlign: "center",
          padding: "16px",
          color: "var(--color-muted)",
          borderTop: "1px solid var(--color-border)",
          fontSize: "0.85rem",
        }}
      >
        CineRate &middot; Movie &amp; Web Series Recommendation + Rating System
      </footer>
    </div>
  );
}

export default App;
