package com.example.demo.colleagues.api;

import jakarta.validation.constraints.Min;

public record AddCupsRequest(
        @Min(value = 1, message = "cups must be at least 1")
        int cups
) {
}
