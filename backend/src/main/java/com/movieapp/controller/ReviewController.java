package com.movieapp.controller;

import com.movieapp.model.Review;
import com.movieapp.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(review));
    }

    @GetMapping("/movie/{movieId}")
    public List<Review> getReviewsForMovie(@PathVariable Integer movieId) {
        return reviewService.getReviewsForMovie(movieId);
    }
}
