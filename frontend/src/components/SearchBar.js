import React, { useState } from "react";

export default function SearchBar({ onSearch, onClear }) {
  const [query, setQuery] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (query.trim()) {
      onSearch(query.trim());
    } else {
      onClear?.();
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: "flex", gap: "8px", marginBottom: "24px" }}>
      <input
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Search movies or web series by name..."
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
          padding: "10px 20px",
          borderRadius: "var(--radius-sm)",
          border: "none",
          backgroundColor: "var(--color-primary)",
          color: "#0F172A",
          fontWeight: 600,
        }}
      >
        Search
      </button>
      {query && (
        <button
          type="button"
          onClick={() => {
            setQuery("");
            onClear?.();
          }}
          style={{
            padding: "10px 16px",
            borderRadius: "var(--radius-sm)",
            border: "1px solid var(--color-border)",
            backgroundColor: "transparent",
            color: "var(--color-text)",
          }}
        >
          Clear
        </button>
      )}
    </form>
  );
}
