package com.example.infrastructure.errors;

import jakarta.ws.rs.core.Response;

public final class UnprocessableEntityStatusCode implements Response.StatusType {

    public static final UnprocessableEntityStatusCode INSTANCE = new UnprocessableEntityStatusCode();

    @Override
    public int getStatusCode() {
        return 422;
    }

    @Override
    public Response.Status.Family getFamily() {
        return Response.Status.Family.CLIENT_ERROR;
    }

    @Override
    public String getReasonPhrase() {
        return "Unprocessable Entity";
    }

}
