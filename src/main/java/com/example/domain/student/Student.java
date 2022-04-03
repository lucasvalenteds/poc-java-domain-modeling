package com.example.domain.student;

import jakarta.validation.Valid;

import java.util.Optional;

public record Student(
    @Valid StudentId id,
    @Valid FirstName firstName,
    @Valid Optional<LastName> lastName
) {
}