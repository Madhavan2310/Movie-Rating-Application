package com.movieapp.controller;

import com.movieapp.model.Movie;
import com.movieapp.service.MovieService;
import com.movieapp.service.TmdbService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;
    private final MovieService movieService;

    public TmdbController(TmdbService tmdbService, MovieService movieService) {
        this.tmdbService = tmdbService;
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String title) {
        if (!tmdbService.isConfigured()) {
            return ResponseEntity.status(503).body(Map.of(
                    "error", "TMDB API key not configured yet. Add it to application.properties."));
        }
        return ResponseEntity.ok(tmdbService.searchMovie(title));
    }

    @GetMapping("/debug-key")
    public ResponseEntity<?> debugKey() {
        String key = tmdbService.isConfigured() ? "CONFIGURED" : "NOT CONFIGURED";
        return ResponseEntity.ok(Map.of("status", key));
    }

    @PostMapping("/import")
    public ResponseEntity<?> importMovie(@RequestParam String title) {
        if (!tmdbService.isConfigured()) {
            return ResponseEntity.status(503).body(Map.of(
                    "error", "TMDB API key not configured yet. Add it to application.properties."));
        }
        try {
            Movie mapped = tmdbService.importByTitle(title);
            Movie saved = movieService.addMovie(mapped);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/import-popular")
    public ResponseEntity<?> importPopular(@RequestParam(defaultValue = "1") int pages) {
        if (!tmdbService.isConfigured()) {
            return ResponseEntity.status(503).body(Map.of(
                    "error", "TMDB API key not configured yet. Add it to application.properties."));
        }

        List<Movie> imported = new ArrayList<>();
        int skipped = 0;

        for (int page = 1; page <= pages; page++) {
            List<Movie> candidates = tmdbService.fetchPopularMovies(page);
            for (Movie candidate : candidates) {
                if (movieService.alreadyImportedFromTmdb(candidate.getTmdbId())) {
                    skipped++;
                    continue;
                }
                imported.add(movieService.addMovie(candidate));
            }
        }

        return ResponseEntity.ok(Map.of(
                "imported", imported.size(),
                "skippedDuplicates", skipped,
                "movies", imported
        ));
    }
}