package com.example.testing;

import org.springframework.dao.DataAccessException;

import java.io.Serial;

public final class DataAccessExceptionStub extends DataAccessException {

    public static final String MESSAGE = "Database error";

    @Serial
    private static final long serialVersionUID = -3421798942859097323L;

    public DataAccessExceptionStub() {
        super(MESSAGE);
    }
}
