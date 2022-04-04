package com.example.infrastructure.validation;

import com.example.testing.Person;
import com.example.testing.PersonResource;
import com.example.testing.PersonService;
import com.example.testing.PersonServiceImpl;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidatableFeatureTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
            .register(PersonResource.class)
            .register(new ValidatableFeature(Map.ofEntries(
                Map.entry(PersonServiceImpl.class, PersonService.class)
            )));
    }

    @Test
    void handlingValidMethodArgument() {
        final var person = new Person(null, "John Smith", 21);

        final var response = target().path("person")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(person));

        response.bufferEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void handlingInvalidMethodArgument() {
        final var person = new Person(null, null, 21);

        final var response = target().path("person")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(person));

        response.bufferEntity();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}