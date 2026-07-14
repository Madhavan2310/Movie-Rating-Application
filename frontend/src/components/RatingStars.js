import React, { useState } from "react";

/**
 * Interactive 1-5 star rating input.
 * Props:
 *  - value: current rating (number, optional)
 *  - onRate: callback(rating) fired on click
 *  - readOnly: if true, just displays the value
 */
export default function RatingStars({ value = 0, onRate, readOnly = false }) {
  const [hover, setHover] = useState(0);
  const display = hover || value;

  return (
    <div style={{ display: "inline-flex", gap: "4px" }}>
      {[1, 2, 3, 4, 5].map((star) => (
        <span
          key={star}
          onMouseEnter={() => !readOnly && setHover(star)}
          onMouseLeave={() => !readOnly && setHover(0)}
          onClick={() => !readOnly && onRate && onRate(star)}
          style={{
            cursor: readOnly ? "default" : "pointer",
            fontSize: "1.4rem",
            color: star <= display ? "var(--color-primary)" : "var(--color-border)",
            transition: "color 0.15s ease",
          }}
        >
          ★
        </span>
      ))}
    </div>
  );
}
