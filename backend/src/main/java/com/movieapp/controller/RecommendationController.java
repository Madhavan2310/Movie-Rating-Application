package com.movieapp.controller;

import com.movieapp.dto.MovieResponse;
import com.movieapp.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // GET /recommendations/{userId}  -> personalized
    @GetMapping("/{userId}")
    public List<MovieResponse> getRecommendations(@PathVariable Integer userId) {
        return recommendationService.personalizedForUser(userId);
    }

    // GET /recommendations/genre/Sci-Fi
    @GetMapping("/genre/{genre}")
    public List<MovieResponse> byGenre(@PathVariable String genre) {
        return recommendationService.byGenre(genre);
    }

    // GET /recommendations/top-rated
    @GetMapping("/top-rated")
    public List<MovieResponse> topRated() {
        return recommendationService.topRated();
    }
}
