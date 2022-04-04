package com.example.domain.course;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record Title(@NotNull @NotEmpty @Length(min = 0, max = 80) String value) {
}
