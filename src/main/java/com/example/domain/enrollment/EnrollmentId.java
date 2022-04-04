package com.example.domain.enrollment;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnrollmentId(@NotNull UUID value) {
}
