package com.movieapp.service;

import com.movieapp.model.Review;
import com.movieapp.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForMovie(Integer movieId) {
        return reviewRepository.findByMovieIdOrderByCreatedAtDesc(movieId);
    }
}
