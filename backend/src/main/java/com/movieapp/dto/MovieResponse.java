package com.movieapp.dto;

import com.movieapp.model.Movie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieResponse {
    private Integer movieId;
    private String title;
    private String genre;
    private Integer releaseYear;
    private String description;
    private String posterUrl;
    private String type;
    private double averageRating;
    private long ratingCount;

    public static MovieResponse from(Movie movie, double averageRating, long ratingCount) {
        MovieResponse dto = new MovieResponse();
        dto.setMovieId(movie.getMovieId());
        dto.setTitle(movie.getTitle());
        dto.setGenre(movie.getGenre());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDescription(movie.getDescription());
        dto.setPosterUrl(movie.getPosterUrl());
        dto.setType(movie.getType());
        dto.setAverageRating(averageRating);
        dto.setRatingCount(ratingCount);
        return dto;
    }
}
