package com.movieapp.repository;

import com.movieapp.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    List<Rating> findByMovieId(Integer movieId);

    List<Rating> findByUserId(Integer userId);

    Optional<Rating> findByUserIdAndMovieId(Integer userId, Integer movieId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.movieId = :movieId")
    Double findAverageRatingByMovieId(@Param("movieId") Integer movieId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.movieId = :movieId")
    Long countByMovieId(@Param("movieId") Integer movieId);
}
