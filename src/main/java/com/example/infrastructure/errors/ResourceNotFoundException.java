package com.example.infrastructure.errors;

import java.io.Serial;

public abstract class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4164228308890459484L;

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
