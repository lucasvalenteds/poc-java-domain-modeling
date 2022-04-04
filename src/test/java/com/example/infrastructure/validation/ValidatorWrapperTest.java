package com.example.infrastructure.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorWrapperTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    record Text(@NotEmpty(message = "Text must be informed") String value) {
    }

    @Test
    void throwingExceptionWhenAnyViolationHappens() {
        final var text = new Text("");

        final var exception = assertThrows(
            ConstraintViolationException.class,
            () -> ValidatorWrapper.validate(validator, text)
        );

        assertThat(exception.getConstraintViolations())
            .hasSize(1);
        assertThat(exception.getConstraintViolations().iterator().next())
            .extracting(ConstraintViolation::getMessage)
            .isEqualTo("Text must be informed");
    }

    @Test
    void returningObjectWhenNoneViolationHappens() {
        final var text = new Text("Hello World!");

        final var textReturned = ValidatorWrapper.validate(validator, text);

        assertEquals(text, textReturned);
    }
}