package com.example.infrastructure.errors;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public final class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionMapper.class);

    @Override
    public Response toResponse(BusinessException exception) {
        LOGGER.info(exception.getMessage(), exception);

        return Response.status(UnprocessableEntityStatusCode.INSTANCE)
            .entity(new ErrorResponse(exception.getMessage()))
            .build();
    }
}
