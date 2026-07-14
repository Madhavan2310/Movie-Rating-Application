package com.movieapp.repository;

import com.movieapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByMovieIdOrderByCreatedAtDesc(Integer movieId);
}
