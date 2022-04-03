package com.example.infrastructure.validation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

public final class ValidatorWrapper {

    public static <T> T validate(final Validator validator, final T object) throws ConstraintViolationException {
        final var violations = validator.validate(object);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return object;
    }
}
