package com.movieapp.controller;

import com.movieapp.dto.MovieResponse;
import com.movieapp.model.Movie;
import com.movieapp.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // GET /movies
    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }

    // GET /movies/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Integer id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    // GET /movies/search?title=inter
    @GetMapping("/search")
    public List<MovieResponse> search(@RequestParam String title) {
        return movieService.searchByTitle(title);
    }

    // GET /movies/trending
    @GetMapping("/trending")
    public List<MovieResponse> trending() {
        return movieService.getTrending(10);
    }

    // POST /movies  (admin: add a movie/web series)
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.addMovie(movie));
    }

    // PUT /movies/{id}  (admin: edit a movie/web series)
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.updateMovie(id, movie));
    }

    // DELETE /movies/{id}  (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
