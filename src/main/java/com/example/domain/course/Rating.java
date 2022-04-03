package com.example.domain.course;

import jakarta.validation.constraints.Min;

public record Rating(@Min(0) int value) {
}
