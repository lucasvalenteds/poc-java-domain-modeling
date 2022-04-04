package com.example.persistence.enrollment;

import com.example.infrastructure.errors.PersistenceException;

import java.io.Serial;

public final class EnrollmentPersistenceException extends PersistenceException {

    @Serial
    private static final long serialVersionUID = 6162041081830753028L;

    public EnrollmentPersistenceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
