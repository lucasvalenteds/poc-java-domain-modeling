package com.example.domain.student;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LastName(@NotNull @Length(min = 2, max = 50) String value) {
}
