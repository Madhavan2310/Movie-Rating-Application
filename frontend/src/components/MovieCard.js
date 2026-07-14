import React from "react";
import { useNavigate } from "react-router-dom";

export default function MovieCard({ movie }) {
  const navigate = useNavigate();

  return (
    <div
      onClick={() => navigate(`/movies/${movie.movieId}`)}
      style={{
        backgroundColor: "var(--color-secondary)",
        borderRadius: "var(--radius-md)",
        overflow: "hidden",
        cursor: "pointer",
        boxShadow: "var(--shadow-card)",
        border: "1px solid var(--color-border)",
        transition: "transform 0.15s ease",
      }}
      onMouseEnter={(e) => (e.currentTarget.style.transform = "translateY(-4px)")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "translateY(0)")}
    >
      <div
        style={{
          height: "220px",
          backgroundColor: "#0b1220",
          backgroundImage: movie.posterUrl ? `url(${movie.posterUrl})` : "none",
          backgroundSize: "cover",
          backgroundPosition: "center",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "var(--color-muted)",
          fontSize: "0.85rem",
        }}
      >
        {!movie.posterUrl && "No Poster"}
      </div>

      <div style={{ padding: "12px 14px" }}>
        <h3 style={{ margin: "0 0 6px", fontSize: "1rem" }}>{movie.title}</h3>
        <p style={{ margin: "0 0 8px", fontSize: "0.8rem", color: "var(--color-muted)" }}>
          {movie.genre} {movie.releaseYear ? `· ${movie.releaseYear}` : ""}
        </p>
        <div style={{ display: "flex", alignItems: "center", gap: "6px" }}>
          <span style={{ color: "var(--color-primary)" }}>★</span>
          <span>{movie.averageRating?.toFixed?.(1) ?? "0.0"}</span>
          <span style={{ color: "var(--color-muted)", fontSize: "0.75rem" }}>
            ({movie.ratingCount ?? 0})
          </span>
        </div>
      </div>
    </div>
  );
}
