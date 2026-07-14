package com.movieapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 50)
    private String genre;

    private Integer releaseYear;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String posterUrl;

    @Column(length = 20)
    private String type = "MOVIE"; // MOVIE or WEB_SERIES

    // Filled in later once looked up via TMDB
    private Integer tmdbId;

    private LocalDateTime createdAt = LocalDateTime.now();
}
