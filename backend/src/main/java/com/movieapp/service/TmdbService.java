package com.movieapp.service;

import com.movieapp.config.TmdbConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Future Enhancement (Step 8): Use TMDB API to fetch movie details automatically.
 *
 * Once you have your TMDB key (https://www.themoviedb.org/settings/api):
 *   1. Paste it into application.properties -> tmdb.api.key
 *   2. Call searchMovie(title) to look a title up on TMDB
 *   3. Map the response fields (overview, poster_path, release_date, genres)
 *      onto your Movie entity in MovieService.addMovie()
 *
 * This class is intentionally minimal so it doesn't break the app if no key is set yet.
 */
@Service
public class TmdbService {

    private final RestTemplate restTemplate;
    private final TmdbConfig tmdbConfig;

    public TmdbService(RestTemplate restTemplate, TmdbConfig tmdbConfig) {
        this.restTemplate = restTemplate;
        this.tmdbConfig = tmdbConfig;
    }

    public boolean isConfigured() {
        return tmdbConfig.getApiKey() != null
                && !tmdbConfig.getApiKey().isBlank()
                && !tmdbConfig.getApiKey().equals("YOUR_TMDB_API_KEY_HERE");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> searchMovie(String title) {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "TMDB API key not set yet. Add it to application.properties (tmdb.api.key).");
        }

        String url = String.format(
                "%s/search/movie?api_key=%s&query=%s",
                tmdbConfig.getBaseUrl(), tmdbConfig.getApiKey(), title);

        return restTemplate.getForObject(url, Map.class);
    }
}
