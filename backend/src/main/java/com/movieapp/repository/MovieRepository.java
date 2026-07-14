package com.movieapp.repository;

import com.movieapp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    // Search Movies by Name (case-insensitive, partial match)
    List<Movie> findByTitleContainingIgnoreCase(String title);

    // Genre-based recommendation: SELECT * FROM movies WHERE genre = ?
    List<Movie> findByGenreIgnoreCase(String genre);

    List<Movie> findByGenreInIgnoreCase(List<String> genres);
}
