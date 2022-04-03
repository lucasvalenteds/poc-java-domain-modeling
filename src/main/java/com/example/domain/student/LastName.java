package com.example.domain.student;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LastName(@NotNull @Size(min = 2, max = 50) String value) {
}
