package com.example.domain.course;

import jakarta.validation.constraints.PositiveOrZero;

public record Rating(@PositiveOrZero int value) {
}
