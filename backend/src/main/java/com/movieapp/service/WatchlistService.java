package com.movieapp.service;

import com.movieapp.model.Watchlist;
import com.movieapp.repository.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public Watchlist addToWatchlist(Integer userId, Integer movieId) {
        return watchlistRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseGet(() -> {
                    Watchlist w = new Watchlist();
                    w.setUserId(userId);
                    w.setMovieId(movieId);
                    return watchlistRepository.save(w);
                });
    }

    public void removeFromWatchlist(Integer userId, Integer movieId) {
        watchlistRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    public List<Watchlist> getWatchlist(Integer userId) {
        return watchlistRepository.findByUserId(userId);
    }
}
