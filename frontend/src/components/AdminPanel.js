import React, { useEffect, useState } from "react";
import { getAllMovies, addMovie, updateMovie, deleteMovie } from "../api/api";
import { useAuth } from "../context/AuthContext";

const EMPTY_FORM = {
  title: "",
  genre: "",
  releaseYear: "",
  description: "",
  posterUrl: "",
  type: "MOVIE",
};

export default function AdminPanel() {
  const { user } = useAuth();
  const [movies, setMovies] = useState([]);
  const [form, setForm] = useState(EMPTY_FORM);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState("");

  const load = async () => {
    const res = await getAllMovies();
    setMovies(res.data);
  };

  useEffect(() => {
    load();
  }, []);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = { ...form, releaseYear: form.releaseYear ? Number(form.releaseYear) : null };
    try {
      if (editingId) {
        await updateMovie(editingId, payload);
        setMessage("Movie updated.");
      } else {
        await addMovie(payload);
        setMessage("Movie added.");
      }
      setForm(EMPTY_FORM);
      setEditingId(null);
      load();
    } catch {
      setMessage("Something went wrong.");
    }
  };

  const handleEdit = (movie) => {
    setEditingId(movie.movieId);
    setForm({
      title: movie.title,
      genre: movie.genre || "",
      releaseYear: movie.releaseYear || "",
      description: movie.description || "",
      posterUrl: movie.posterUrl || "",
      type: movie.type || "MOVIE",
    });
  };

  const handleDelete = async (id) => {
    await deleteMovie(id);
    load();
  };

  // Not a hard security boundary (that lives on the backend) -- just keeps
  // the admin UI out of the way for regular visitors.
  if (!user || user.role !== "ADMIN") {
    return (
      <p style={{ color: "var(--color-muted)" }}>
        Admin access only. Log in with an admin account (set role='ADMIN' on the user in the database)
        to manage the movie catalog.
      </p>
    );
  }

  return (
    <div>
      <h1>Admin Panel</h1>
      <p style={{ color: "var(--color-muted)" }}>Add or edit movies &amp; web series in the catalog.</p>

      <form onSubmit={handleSubmit} style={formStyle}>
        <input name="title" placeholder="Title" value={form.title} onChange={handleChange} required style={inputStyle} />
        <input name="genre" placeholder="Genre (e.g. Sci-Fi)" value={form.genre} onChange={handleChange} style={inputStyle} />
        <input name="releaseYear" placeholder="Release Year" value={form.releaseYear} onChange={handleChange} style={inputStyle} />
        <select name="type" value={form.type} onChange={handleChange} style={inputStyle}>
          <option value="MOVIE">Movie</option>
          <option value="WEB_SERIES">Web Series</option>
        </select>
        <input name="posterUrl" placeholder="Poster URL (optional)" value={form.posterUrl} onChange={handleChange} style={inputStyle} />
        <textarea name="description" placeholder="Description" value={form.description} onChange={handleChange} rows={3} style={inputStyle} />
        <button type="submit" style={buttonStyle}>
          {editingId ? "Update Movie" : "Add Movie"}
        </button>
        {editingId && (
          <button
            type="button"
            onClick={() => { setEditingId(null); setForm(EMPTY_FORM); }}
            style={{ ...buttonStyle, background: "transparent", border: "1px solid var(--color-border)", color: "var(--color-text)" }}
          >
            Cancel
          </button>
        )}
      </form>

      {message && <p style={{ color: "var(--color-primary)" }}>{message}</p>}

      <h2 style={{ marginTop: "32px" }}>Catalog</h2>
      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr style={{ textAlign: "left", borderBottom: "1px solid var(--color-border)" }}>
            <th style={thStyle}>Title</th>
            <th style={thStyle}>Genre</th>
            <th style={thStyle}>Year</th>
            <th style={thStyle}>Avg Rating</th>
            <th style={thStyle}></th>
          </tr>
        </thead>
        <tbody>
          {movies.map((m) => (
            <tr key={m.movieId} style={{ borderBottom: "1px solid var(--color-border)" }}>
              <td style={tdStyle}>{m.title}</td>
              <td style={tdStyle}>{m.genre}</td>
              <td style={tdStyle}>{m.releaseYear}</td>
              <td style={tdStyle}>{m.averageRating?.toFixed(1)}</td>
              <td style={tdStyle}>
                <button onClick={() => handleEdit(m)} style={linkButton}>Edit</button>
                <button onClick={() => handleDelete(m.movieId)} style={{ ...linkButton, color: "var(--color-danger)" }}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const formStyle = {
  display: "grid",
  gridTemplateColumns: "1fr 1fr",
  gap: "12px",
  backgroundColor: "var(--color-secondary)",
  padding: "24px",
  borderRadius: "var(--radius-md)",
  border: "1px solid var(--color-border)",
};

const inputStyle = {
  padding: "10px 14px",
  borderRadius: "var(--radius-sm)",
  border: "1px solid var(--color-border)",
  backgroundColor: "var(--color-bg)",
  color: "var(--color-text)",
};

const buttonStyle = {
  padding: "12px",
  borderRadius: "var(--radius-sm)",
  border: "none",
  backgroundColor: "var(--color-primary)",
  color: "#0F172A",
  fontWeight: 700,
};

const thStyle = { padding: "10px 8px", color: "var(--color-muted)" };
const tdStyle = { padding: "10px 8px" };
const linkButton = {
  background: "none",
  border: "none",
  color: "var(--color-primary)",
  marginRight: "12px",
  padding: 0,
};
