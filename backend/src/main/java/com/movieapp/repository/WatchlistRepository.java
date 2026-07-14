package com.movieapp.repository;

import com.movieapp.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {
    List<Watchlist> findByUserId(Integer userId);
    Optional<Watchlist> findByUserIdAndMovieId(Integer userId, Integer movieId);
    void deleteByUserIdAndMovieId(Integer userId, Integer movieId);
}
