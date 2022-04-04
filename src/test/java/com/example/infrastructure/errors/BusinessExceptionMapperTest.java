package com.example.infrastructure.errors;

import com.example.testing.ErrorResource;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessExceptionMapperTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
            .register(ErrorResource.class)
            .register(BusinessExceptionMapper.class)
            .register(JacksonFeature.class);
    }

    @Test
    void handlingBusinessExceptionAsUnprocessableEntity() {
        final var response = target().path("/errors/business")
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .get();

        response.bufferEntity();

        assertEquals(UnprocessableEntityStatusCode.INSTANCE.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(ErrorResponse.class))
            .extracting(ErrorResponse::message)
            .isEqualTo("Some error");
    }
}