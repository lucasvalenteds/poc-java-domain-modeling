package com.example.domain.student;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FirstName(@NotNull @Size(min = 2, max = 30) String value) {
}
