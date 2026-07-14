package com.movieapp.controller;

import com.movieapp.service.TmdbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Step 8 hook: once tmdb.api.key is set in application.properties,
 * this endpoint lets the frontend (or admin panel) search TMDB directly,
 * e.g. GET /tmdb/search?title=Interstellar
 */
@RestController
@RequestMapping("/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String title) {
        if (!tmdbService.isConfigured()) {
            return ResponseEntity.status(503).body(Map.of(
                    "error", "TMDB API key not configured yet. Add it to application.properties."));
        }
        return ResponseEntity.ok(tmdbService.searchMovie(title));
    }
}
