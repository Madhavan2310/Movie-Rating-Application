package com.movieapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class AuthDtos {

    @Getter
    @Setter
    public static class RegisterRequest {
        @NotBlank
        private String username;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }
}
