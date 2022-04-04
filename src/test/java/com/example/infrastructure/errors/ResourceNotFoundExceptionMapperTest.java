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

class ResourceNotFoundExceptionMapperTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
            .register(ErrorResource.class)
            .register(ResourceNotFoundExceptionMapper.class)
            .register(JacksonFeature.class);
    }

    @Test
    void handlingResourceNotFoundExceptionAsNotFound() {
        final var response = target().path("/errors/resource-not-found")
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .get();

        response.bufferEntity();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(ErrorResponse.class))
            .extracting(ErrorResponse::message)
            .isEqualTo("Course not found by ID");
    }
}