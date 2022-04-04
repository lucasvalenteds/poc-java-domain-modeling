package com.example.infrastructure.errors;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Provider
public final class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException exception) {
        LOGGER.info(exception.getMessage(), exception);

        return Response.status(UnprocessableEntityStatusCode.INSTANCE)
            .entity(Map.of("message", exception.getMessage()))
            .build();
    }
}
