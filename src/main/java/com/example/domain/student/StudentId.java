package com.example.domain.student;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentId(@NotNull UUID value) {
}
