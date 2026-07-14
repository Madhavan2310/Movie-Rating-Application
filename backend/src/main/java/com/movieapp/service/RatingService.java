package com.movieapp.service;

import com.movieapp.dto.RatingRequest;
import com.movieapp.model.Rating;
import com.movieapp.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    // One rating per user per movie: create it, or update it if it already exists
    public Rating rateMovie(RatingRequest request) {
        Optional<Rating> existing = ratingRepository.findByUserIdAndMovieId(
                request.getUserId(), request.getMovieId());

        Rating rating = existing.orElseGet(Rating::new);
        rating.setUserId(request.getUserId());
        rating.setMovieId(request.getMovieId());
        rating.setRating(request.getRating());
        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsForMovie(Integer movieId) {
        return ratingRepository.findByMovieId(movieId);
    }

    /**
     * Step 4: Implement Rating System
     * Average = sum(ratings) / count(ratings)
     */
    public double calculateAverageRating(Integer movieId) {
        List<Rating> ratings = ratingRepository.findByMovieId(movieId);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        int sum = 0;
        for (Rating r : ratings) {
            sum += r.getRating();
        }
        return Math.round((sum / (double) ratings.size()) * 100.0) / 100.0; // 2 decimal places
    }

    public long getRatingCount(Integer movieId) {
        return ratingRepository.countByMovieId(movieId);
    }
}
