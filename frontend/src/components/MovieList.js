import React, { useEffect, useState } from "react";
import { getAllMovies, searchMovies } from "../api/api";
import MovieCard from "./MovieCard";
import SearchBar from "./SearchBar";

export default function MovieList() {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchAll = async () => {
    setLoading(true);
    try {
      const res = await getAllMovies();
      setMovies(res.data);
      setError("");
    } catch (err) {
      setError("Could not load movies. Is the backend running on http://localhost:8080?");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAll();
  }, []);

  const handleSearch = async (title) => {
    setLoading(true);
    try {
      const res = await searchMovies(title);
      setMovies(res.data);
      setError("");
    } catch (err) {
      setError("Search failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 style={{ marginBottom: "8px" }}>Browse Movies &amp; Web Series</h1>
      <p style={{ color: "var(--color-muted)", marginTop: 0 }}>
        Discover, rate, and get recommendations tailored to you.
      </p>

      <SearchBar onSearch={handleSearch} onClear={fetchAll} />

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: "var(--color-danger)" }}>{error}</p>}

      {!loading && !error && movies.length === 0 && (
        <p style={{ color: "var(--color-muted)" }}>
          No titles yet. Use the Admin panel to add your first movie.
        </p>
      )}

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))",
          gap: "20px",
        }}
      >
        {movies.map((movie) => (
          <MovieCard key={movie.movieId} movie={movie} />
        ))}
      </div>
    </div>
  );
}
