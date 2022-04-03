package com.example;

import com.example.infrastructure.configuration.DatabaseFeature;
import com.example.web.enrollment.EnrollResponse;
import com.example.web.enrollment.RateRequest;
import com.example.web.enrollment.RateResponse;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MainInMemoryTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
            .packages(Main.class.getPackageName())
            .property(DatabaseFeature.DATABASE_URL_PROPERTY, "jdbc:h2:mem:testdb1;DB_CLOSE_DELAY=-1")
            .property(DatabaseFeature.DATABASE_USER_PROPERTY, "sa")
            .property(DatabaseFeature.DATABASE_PASSWORD_PROPERTY, "password");
    }

    private static String studentId;
    private static String courseId;

    @Test
    @Order(1)
    void creatingStudent() {
        final var response = target().path("/students")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(Map.of(
                "firstName", "John",
                "lastName", "Smith"
            )));

        response.bufferEntity();

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());

        studentId = this.getLastResourceURI(response.getLocation());
    }

    @Test
    @Order(2)
    void creatingCourse() {
        final var response = target().path("/courses")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(Map.of(
                "code", "ALG123",
                "title", "Introduction to Algorithms"
            )));

        response.bufferEntity();

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());

        courseId = this.getLastResourceURI(response.getLocation());
    }

    @Test
    @Order(3)
    void studentCourseEnrollment() {
        final var response = target().path("/enrollments/courses/" + courseId + "/students/" + studentId + "/enroll")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(""));

        response.bufferEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        final var enrollmentResponse = response.readEntity(EnrollResponse.class);
        assertNotNull(enrollmentResponse);
        assertNotNull(enrollmentResponse.id());
        assertEquals(UUID.fromString(studentId), enrollmentResponse.studentId());
        assertEquals(UUID.fromString(courseId), enrollmentResponse.courseId());
    }

    @Test
    @Order(4)
    void studentCourseRating() {
        final var response = target().path("/enrollments/courses/" + courseId + "/students/" + studentId + "/rate")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(new RateRequest(3)));

        response.bufferEntity();

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());

        final var rateResponse = response.readEntity(RateResponse.class);
        assertNotNull(rateResponse);
        assertEquals(3, rateResponse.rating());
    }

    private String getLastResourceURI(final URI uri) {
        final var chunks = uri.toString().split("/");

        return chunks[chunks.length - 1];
    }
}