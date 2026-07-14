package com.movieapp.service;

import com.movieapp.dto.MovieResponse;
import com.movieapp.model.Movie;
import com.movieapp.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RatingService ratingService;

    public MovieService(MovieRepository movieRepository, RatingService ratingService) {
        this.movieRepository = movieRepository;
        this.ratingService = ratingService;
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Integer id, Movie updated) {
        Movie existing = getMovieEntity(id);
        existing.setTitle(updated.getTitle());
        existing.setGenre(updated.getGenre());
        existing.setReleaseYear(updated.getReleaseYear());
        existing.setDescription(updated.getDescription());
        existing.setPosterUrl(updated.getPosterUrl());
        existing.setType(updated.getType());
        return movieRepository.save(existing);
    }

    public void deleteMovie(Integer id) {
        movieRepository.deleteById(id);
    }

    public Movie getMovieEntity(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
    }

    public MovieResponse getMovieById(Integer id) {
        return toResponse(getMovieEntity(id));
    }

    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream().map(this::toResponse).toList();
    }

    // Search Movies by Name
    public List<MovieResponse> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title)
                .stream().map(this::toResponse).toList();
    }

    // Genre-based recommendation: SELECT * FROM movies WHERE genre = ?
    public List<MovieResponse> getByGenre(String genre) {
        return movieRepository.findByGenreIgnoreCase(genre)
                .stream().map(this::toResponse).toList();
    }

    // Trending: highest average rating first
    public List<MovieResponse> getTrending(int limit) {
        return movieRepository.findAll().stream()
                .map(this::toResponse)
                .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
                .limit(limit)
                .toList();
    }

    private MovieResponse toResponse(Movie movie) {
        double avg = ratingService.calculateAverageRating(movie.getMovieId());
        long count = ratingService.getRatingCount(movie.getMovieId());
        return MovieResponse.from(movie, avg, count);
    }
}
