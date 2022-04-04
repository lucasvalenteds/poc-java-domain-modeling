package com.example.persistence.rating;

import com.example.infrastructure.errors.PersistenceException;

import java.io.Serial;

public final class RatingPersistenceException extends PersistenceException {

    @Serial
    private static final long serialVersionUID = 8505686053610483971L;

    public RatingPersistenceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
