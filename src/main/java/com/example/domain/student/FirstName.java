package com.example.domain.student;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record FirstName(@NotNull @Length(min = 2, max = 30) String value) {
}
