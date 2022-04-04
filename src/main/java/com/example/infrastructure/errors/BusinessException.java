package com.example.infrastructure.errors;

import java.io.Serial;

public final class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 746695408842766841L;

    public BusinessException(String message) {
        super(message);
    }
}
