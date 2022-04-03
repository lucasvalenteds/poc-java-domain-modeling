package com.example.persistence.rating;

import java.io.Serial;

public final class RatingPersistenceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8505686053610483971L;

    public RatingPersistenceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
