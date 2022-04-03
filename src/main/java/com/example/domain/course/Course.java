package com.example.domain.course;

import jakarta.validation.Valid;

public record Course(
    @Valid CourseId id,
    @Valid Code code,
    @Valid Title title,
    @Valid Rating rating
) {
}