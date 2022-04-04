package com.example.domain.course;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record Code(
    @NotNull
    @NotEmpty
    @Pattern(regexp = "[A-Z]{3}[0-9]{4}")
    String value
) {
}
