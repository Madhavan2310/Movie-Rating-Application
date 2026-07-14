package com.movieapp.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Holds TMDB API settings. Once you have your key from
 * https://www.themoviedb.org/settings/api, paste it into
 * application.properties under tmdb.api.key and TmdbService
 * will be able to fetch/auto-fill movie details.
 */
@Configuration
@Getter
public class TmdbConfig {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base-url}")
    private String baseUrl;

    @Value("${tmdb.image.base-url}")
    private String imageBaseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
