import React, { useEffect, useState, useCallback } from "react";
import { getWatchlist, getMovieById, removeFromWatchlist } from "../api/api";
import MovieCard from "./MovieCard";
import { useAuth } from "../context/AuthContext";

export default function Watchlist() {
  const { user } = useAuth();
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    if (!user) return;
    setLoading(true);
    const listRes = await getWatchlist(user.id);
    const movieDetails = await Promise.all(
      listRes.data.map((entry) => getMovieById(entry.movieId).then((r) => r.data))
    );
    setMovies(movieDetails);
    setLoading(false);
  }, [user]);

  useEffect(() => {
    load();
  }, [load]);

  const handleRemove = async (movieId) => {
    await removeFromWatchlist(user.id, movieId);
    load();
  };

  if (!user) {
    return <p style={{ color: "var(--color-muted)" }}>Log in to view your watchlist.</p>;
  }

  return (
    <div>
      <h1>Your Watchlist</h1>
      {loading && <p>Loading...</p>}
      {!loading && movies.length === 0 && (
        <p style={{ color: "var(--color-muted)" }}>Your watchlist is empty. Add titles from their detail page.</p>
      )}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))",
          gap: "20px",
        }}
      >
        {movies.map((movie) => (
          <div key={movie.movieId}>
            <MovieCard movie={movie} />
            <button
              onClick={() => handleRemove(movie.movieId)}
              style={{
                marginTop: "8px",
                width: "100%",
                padding: "8px",
                borderRadius: "var(--radius-sm)",
                border: "1px solid var(--color-danger)",
                background: "transparent",
                color: "var(--color-danger)",
              }}
            >
              Remove
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
