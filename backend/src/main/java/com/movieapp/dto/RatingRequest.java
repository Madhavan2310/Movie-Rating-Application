package com.movieapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {

    @NotNull
    private Integer userId;

    @NotNull
    private Integer movieId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
