package com.movieapp.service;

import com.movieapp.config.TmdbConfig;
import com.movieapp.model.Movie;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TmdbService {

    private final RestTemplate restTemplate;
    private final TmdbConfig tmdbConfig;
    private Map<Integer, String> genreCache;

    public TmdbService(RestTemplate restTemplate, TmdbConfig tmdbConfig) {
        this.restTemplate = restTemplate;
        this.tmdbConfig = tmdbConfig;
    }





    public boolean isConfigured() {
        String key = tmdbConfig.getApiKey();
        System.out.println("TMDB CONFIG CHECK >>> [" + key + "] LENGTH=" + (key == null ? "null" : key.length()));
        return key != null
                && !key.isBlank()
                && !key.equals("YOUR_TMDB_API_KEY_HERE");
    }

    private void requireConfigured() {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "TMDB API key not set yet. Add it to application.properties (tmdb.api.key).");
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> searchMovie(String title) {
        requireConfigured();
        String encoded = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String url = String.format(
                "%s/search/movie?api_key=%s&query=%s",
                tmdbConfig.getBaseUrl(), tmdbConfig.getApiKey(), encoded);
        return restTemplate.getForObject(url, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMovieDetails(long tmdbId) {
        requireConfigured();
        String url = String.format(
                "%s/movie/%d?api_key=%s",
                tmdbConfig.getBaseUrl(), tmdbId, tmdbConfig.getApiKey());
        return restTemplate.getForObject(url, Map.class);
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, String> getGenreMap() {
        if (genreCache != null) {
            return genreCache;
        }
        requireConfigured();
        String url = String.format("%s/genre/movie/list?api_key=%s", tmdbConfig.getBaseUrl(), tmdbConfig.getApiKey());
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<Integer, String> map = new HashMap<>();
        List<Map<String, Object>> genres = (List<Map<String, Object>>) response.get("genres");
        if (genres != null) {
            for (Map<String, Object> g : genres) {
                map.put(((Number) g.get("id")).intValue(), (String) g.get("name"));
            }
        }
        genreCache = map;
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<Movie> fetchPopularMovies(int page) {
        requireConfigured();
        String url = String.format(
                "%s/movie/popular?api_key=%s&page=%d",
                tmdbConfig.getBaseUrl(), tmdbConfig.getApiKey(), page);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        Map<Integer, String> genreMap = getGenreMap();
        return results.stream().map(r -> mapListResultToMovie(r, genreMap)).toList();
    }

    @SuppressWarnings("unchecked")
    private Movie mapListResultToMovie(Map<String, Object> result, Map<Integer, String> genreMap) {
        Movie movie = new Movie();
        movie.setTitle((String) result.get("title"));
        movie.setDescription((String) result.get("overview"));
        movie.setType("MOVIE");

        Number id = (Number) result.get("id");
        if (id != null) {
            movie.setTmdbId(id.intValue());
        }

        String posterPath = (String) result.get("poster_path");
        if (posterPath != null) {
            movie.setPosterUrl(tmdbConfig.getImageBaseUrl() + posterPath);
        }

        String releaseDate = (String) result.get("release_date");
        if (releaseDate != null && releaseDate.length() >= 4) {
            movie.setReleaseYear(Integer.parseInt(releaseDate.substring(0, 4)));
        }

        List<Number> genreIds = (List<Number>) result.get("genre_ids");
        if (genreIds != null && !genreIds.isEmpty()) {
            movie.setGenre(genreMap.get(genreIds.get(0).intValue()));
        }

        return movie;
    }

    @SuppressWarnings("unchecked")
    public Movie importByTitle(String title) {
        Map<String, Object> searchResult = searchMovie(title);
        List<Map<String, Object>> results = (List<Map<String, Object>>) searchResult.get("results");

        if (results == null || results.isEmpty()) {
            throw new IllegalArgumentException("No TMDB results found for \"" + title + "\"");
        }

        Number tmdbId = (Number) results.get(0).get("id");
        Map<String, Object> details = getMovieDetails(tmdbId.longValue());
        return mapToMovie(details);
    }

    @SuppressWarnings("unchecked")
    public Movie mapToMovie(Map<String, Object> details) {
        Movie movie = new Movie();
        movie.setTitle((String) details.get("title"));
        movie.setDescription((String) details.get("overview"));
        movie.setType("MOVIE");

        Number id = (Number) details.get("id");
        if (id != null) {
            movie.setTmdbId(id.intValue());
        }

        String posterPath = (String) details.get("poster_path");
        if (posterPath != null) {
            movie.setPosterUrl(tmdbConfig.getImageBaseUrl() + posterPath);
        }

        String releaseDate = (String) details.get("release_date");
        if (releaseDate != null && releaseDate.length() >= 4) {
            movie.setReleaseYear(Integer.parseInt(releaseDate.substring(0, 4)));
        }

        List<Map<String, Object>> genres = (List<Map<String, Object>>) details.get("genres");
        if (genres != null && !genres.isEmpty()) {
            movie.setGenre((String) genres.get(0).get("name"));
        }

        return movie;
    }
}