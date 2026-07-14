package com.movieapp.controller;

import com.movieapp.dto.RatingRequest;
import com.movieapp.model.Rating;
import com.movieapp.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // POST /ratings  { "userId": 1, "movieId": 3, "rating": 5 }
    @PostMapping
    public ResponseEntity<Rating> rateMovie(@Valid @RequestBody RatingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.rateMovie(request));
    }

    // GET /ratings/movie/{movieId}
    @GetMapping("/movie/{movieId}")
    public List<Rating> getRatingsForMovie(@PathVariable Integer movieId) {
        return ratingService.getRatingsForMovie(movieId);
    }

    // GET /ratings/movie/{movieId}/average
    @GetMapping("/movie/{movieId}/average")
    public Map<String, Object> getAverage(@PathVariable Integer movieId) {
        return Map.of(
                "movieId", movieId,
                "averageRating", ratingService.calculateAverageRating(movieId),
                "ratingCount", ratingService.getRatingCount(movieId)
        );
    }
}
