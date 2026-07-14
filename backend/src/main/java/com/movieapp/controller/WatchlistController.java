package com.movieapp.controller;

import com.movieapp.model.Watchlist;
import com.movieapp.service.WatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping("/{userId}/{movieId}")
    public ResponseEntity<Watchlist> add(@PathVariable Integer userId, @PathVariable Integer movieId) {
        return ResponseEntity.ok(watchlistService.addToWatchlist(userId, movieId));
    }

    @DeleteMapping("/{userId}/{movieId}")
    public ResponseEntity<Void> remove(@PathVariable Integer userId, @PathVariable Integer movieId) {
        watchlistService.removeFromWatchlist(userId, movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public List<Watchlist> getWatchlist(@PathVariable Integer userId) {
        return watchlistService.getWatchlist(userId);
    }
}
