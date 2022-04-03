package com.example.domain.course;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseId(@NotNull UUID value) {
}
