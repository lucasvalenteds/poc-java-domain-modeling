package com.example.infrastructure.errors;

import com.example.testing.ErrorResource;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistenceExceptionMapperTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
            .register(ErrorResource.class)
            .register(PersistenceExceptionMapper.class)
            .register(JacksonFeature.class);
    }

    @Test
    void handlingPersistenceExceptionAsInternalError() {
        final var response = target().path("/errors/persistence")
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .get();

        response.bufferEntity();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(ErrorResponse.class))
            .extracting(ErrorResponse::message)
            .isEqualTo("Message");
    }
}