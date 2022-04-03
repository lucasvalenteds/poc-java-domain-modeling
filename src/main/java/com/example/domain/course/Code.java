package com.example.domain.course;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record Code(@NotNull @NotEmpty String value) {
}
