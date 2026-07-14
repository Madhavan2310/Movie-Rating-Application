package com.movieapp.service;

import com.movieapp.dto.MovieResponse;
import com.movieapp.model.Rating;
import com.movieapp.repository.MovieRepository;
import com.movieapp.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final MovieService movieService;

    public RecommendationService(MovieRepository movieRepository,
                                  RatingRepository ratingRepository,
                                  MovieService movieService) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.movieService = movieService;
    }

    /**
     * Level 1: Genre-Based Recommendation
     * SELECT * FROM movies WHERE genre = :genre
     */
    public List<MovieResponse> byGenre(String genre) {
        return movieService.getByGenre(genre);
    }

    /**
     * Level 2: Rating-Based Recommendation
     * Recommend movies with average rating >= 4.0
     */
    public List<MovieResponse> topRated() {
        return movieService.getAllMovies().stream()
                .filter(m -> m.getAverageRating() >= 4.0)
                .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
                .toList();
    }

    /**
     * Level 3: Personalized Recommendation
     * Looks at the genres of movies this user rated 4 or 5 stars,
     * then recommends other movies (not yet rated by the user) from those genres,
     * ranked by average rating.
     */
    public List<MovieResponse> personalizedForUser(Integer userId) {
        List<Rating> userRatings = ratingRepository.findByUserId(userId);

        if (userRatings.isEmpty()) {
            // No history yet -> fall back to trending movies
            return movieService.getTrending(10);
        }

        Set<Integer> ratedMovieIds = userRatings.stream()
                .map(Rating::getMovieId)
                .collect(Collectors.toSet());

        // Genres the user tends to rate highly (preference profile)
        Set<String> preferredGenres = userRatings.stream()
                .filter(r -> r.getRating() >= 4)
                .map(r -> movieRepository.findById(r.getMovieId()).orElse(null))
                .filter(Objects::nonNull)
                .map(m -> m.getGenre())
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        if (preferredGenres.isEmpty()) {
            return movieService.getTrending(10);
        }

        return movieService.getAllMovies().stream()
                .filter(m -> !ratedMovieIds.contains(m.getMovieId()))
                .filter(m -> m.getGenre() != null && preferredGenres.contains(m.getGenre().toLowerCase()))
                .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
                .toList();
    }
}
