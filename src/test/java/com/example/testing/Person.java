package com.example.testing;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record Person(
    UUID id,
    @NotNull @NotEmpty String name,
    @Min(18) @Max(100) int age
) {
}
