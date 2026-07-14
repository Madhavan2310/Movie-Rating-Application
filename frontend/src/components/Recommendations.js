import React, { useEffect, useState } from "react";
import {
  getPersonalizedRecommendations,
  getTopRated,
  getRecommendationsByGenre,
} from "../api/api";
import MovieCard from "./MovieCard";
import { useAuth } from "../context/AuthContext";

const GENRES = ["Action", "Sci-Fi", "Thriller", "Drama", "Comedy", "Romance", "Horror", "Fantasy"];

export default function Recommendations() {
  const { user } = useAuth();
  const [personalized, setPersonalized] = useState([]);
  const [topRated, setTopRated] = useState([]);
  const [genre, setGenre] = useState("Action");
  const [genreMovies, setGenreMovies] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      try {
        const [topRes, genreRes] = await Promise.all([
          getTopRated(),
          getRecommendationsByGenre(genre),
        ]);
        setTopRated(topRes.data);
        setGenreMovies(genreRes.data);

        if (user) {
          const personalRes = await getPersonalizedRecommendations(user.id);
          setPersonalized(personalRes.data);
        }
      } finally {
        setLoading(false);
      }
    };
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [genre, user]);

  return (
    <div>
      <h1>Recommended For You</h1>

      {!user && (
        <p style={{ color: "var(--color-muted)" }}>
          Log in to see recommendations personalized to your ratings.
        </p>
      )}

      {user && (
        <Section title="Personalized For You" movies={personalized} loading={loading} />
      )}

      <Section title="Top Rated (4★ and above)" movies={topRated} loading={loading} />

      <div style={{ margin: "24px 0 12px", display: "flex", alignItems: "center", gap: "12px" }}>
        <h2 style={{ margin: 0 }}>By Genre</h2>
        <select
          value={genre}
          onChange={(e) => setGenre(e.target.value)}
          style={{
            padding: "6px 10px",
            borderRadius: "var(--radius-sm)",
            backgroundColor: "var(--color-secondary)",
            color: "var(--color-text)",
            border: "1px solid var(--color-border)",
          }}
        >
          {GENRES.map((g) => (
            <option key={g} value={g}>{g}</option>
          ))}
        </select>
      </div>
      <Grid movies={genreMovies} loading={loading} />
    </div>
  );
}

function Section({ title, movies, loading }) {
  return (
    <div style={{ marginBottom: "32px" }}>
      <h2>{title}</h2>
      <Grid movies={movies} loading={loading} />
    </div>
  );
}

function Grid({ movies, loading }) {
  if (loading) return <p>Loading...</p>;
  if (movies.length === 0) return <p style={{ color: "var(--color-muted)" }}>Nothing to show yet.</p>;
  return (
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
  );
}
