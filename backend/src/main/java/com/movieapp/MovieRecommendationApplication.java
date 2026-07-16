package com.movieapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieRecommendationApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(MovieRecommendationApplication.class, args);
        System.out.println("TMDB KEY LOADED >>> [" + context.getEnvironment().getProperty("tmdb.api.key") + "]");
    }
}