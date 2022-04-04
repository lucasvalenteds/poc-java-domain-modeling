package com.example.infrastructure.errors;

import java.io.Serial;

public abstract class PersistenceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5208968287084966984L;

    protected PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
