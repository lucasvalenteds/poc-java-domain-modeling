package com.example.persistence.enrollment;

import java.io.Serial;

public final class EnrollmentPersistenceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6162041081830753028L;

    public EnrollmentPersistenceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
