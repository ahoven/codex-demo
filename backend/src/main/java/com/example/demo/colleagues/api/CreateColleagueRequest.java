package com.example.demo.colleagues.api;

import jakarta.validation.constraints.NotBlank;

public record CreateColleagueRequest(
        @NotBlank(message = "name is required")
        String name
) {
}
