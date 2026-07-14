import React, { useEffect, useState, useCallback } from "react";
import { useParams } from "react-router-dom";
import {
  getMovieById,
  rateMovie,
  addReview,
  getReviewsForMovie,
  addToWatchlist,
} from "../api/api";
import RatingStars from "./RatingStars";
import { useAuth } from "../context/AuthContext";

export default function MovieDetail() {
  const { id } = useParams();
  const { user } = useAuth();
  const [movie, setMovie] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [comment, setComment] = useState("");
  const [message, setMessage] = useState("");

  const loadMovie = useCallback(async () => {
    const res = await getMovieById(id);
    setMovie(res.data);
  }, [id]);

  const loadReviews = useCallback(async () => {
    const res = await getReviewsForMovie(id);
    setReviews(res.data);
  }, [id]);

  useEffect(() => {
    loadMovie();
    loadReviews();
  }, [loadMovie, loadReviews]);

  const handleRate = async (stars) => {
    if (!user) {
      setMessage("Please log in to rate this title.");
      return;
    }
    await rateMovie({ userId: user.id, movieId: Number(id), rating: stars });
    setMessage(`You rated this ${stars} star${stars > 1 ? "s" : ""}!`);
    loadMovie();
  };

  const handleAddToWatchlist = async () => {
    if (!user) {
      setMessage("Please log in to use your watchlist.");
      return;
    }
    await addToWatchlist(user.id, Number(id));
    setMessage("Added to your watchlist.");
  };

  const handleSubmitReview = async (e) => {
    e.preventDefault();
    if (!user) {
      setMessage("Please log in to leave a review.");
      return;
    }
    if (!comment.trim()) return;
    await addReview({ userId: user.id, movieId: Number(id), comment });
    setComment("");
    loadReviews();
  };

  if (!movie) return <p>Loading...</p>;

  return (
    <div style={{ display: "grid", gridTemplateColumns: "280px 1fr", gap: "32px" }}>
      <div
        style={{
          height: "380px",
          borderRadius: "var(--radius-md)",
          backgroundColor: "var(--color-secondary)",
          backgroundImage: movie.posterUrl ? `url(${movie.posterUrl})` : "none",
          backgroundSize: "cover",
          backgroundPosition: "center",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "var(--color-muted)",
        }}
      >
        {!movie.posterUrl && "No Poster"}
      </div>

      <div>
        <h1 style={{ margin: "0 0 4px" }}>{movie.title}</h1>
        <p style={{ color: "var(--color-muted)", marginTop: 0 }}>
          {movie.genre} {movie.releaseYear ? `· ${movie.releaseYear}` : ""} ·{" "}
          {movie.type === "WEB_SERIES" ? "Web Series" : "Movie"}
        </p>

        <div style={{ display: "flex", alignItems: "center", gap: "10px", margin: "16px 0" }}>
          <RatingStars value={Math.round(movie.averageRating)} readOnly />
          <strong>{movie.averageRating?.toFixed(2)}</strong>
          <span style={{ color: "var(--color-muted)" }}>({movie.ratingCount} ratings)</span>
        </div>

        <p style={{ lineHeight: 1.6 }}>{movie.description || "No description available."}</p>

        <div style={{ margin: "20px 0" }}>
          <p style={{ marginBottom: "6px" }}>Rate this title:</p>
          <RatingStars onRate={handleRate} />
        </div>

        <button
          onClick={handleAddToWatchlist}
          style={{
            padding: "10px 18px",
            borderRadius: "var(--radius-sm)",
            border: "1px solid var(--color-primary)",
            background: "transparent",
            color: "var(--color-primary)",
            fontWeight: 600,
          }}
        >
          + Add to Watchlist
        </button>

        {message && <p style={{ color: "var(--color-primary)", marginTop: "12px" }}>{message}</p>}

        <hr style={{ margin: "28px 0", border: "none", borderTop: "1px solid var(--color-border)" }} />

        <h2>Reviews</h2>
        <form onSubmit={handleSubmitReview} style={{ display: "flex", gap: "8px", marginBottom: "16px" }}>
          <input
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="Share your thoughts..."
            style={{
              flex: 1,
              padding: "10px 14px",
              borderRadius: "var(--radius-sm)",
              border: "1px solid var(--color-border)",
              backgroundColor: "var(--color-secondary)",
              color: "var(--color-text)",
            }}
          />
          <button
            type="submit"
            style={{
              padding: "10px 18px",
              borderRadius: "var(--radius-sm)",
              border: "none",
              backgroundColor: "var(--color-primary)",
              color: "#0F172A",
              fontWeight: 600,
            }}
          >
            Post
          </button>
        </form>

        {reviews.length === 0 && <p style={{ color: "var(--color-muted)" }}>No reviews yet.</p>}
        {reviews.map((r) => (
          <div
            key={r.reviewId}
            style={{
              backgroundColor: "var(--color-secondary)",
              borderRadius: "var(--radius-sm)",
              padding: "12px 16px",
              marginBottom: "10px",
            }}
          >
            <p style={{ margin: 0 }}>{r.comment}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
