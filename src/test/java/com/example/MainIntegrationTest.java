package com.example;

import com.example.infrastructure.errors.UnprocessableEntityStatusCode;
import com.example.web.enrollment.EnrollResponse;
import com.example.web.enrollment.RateRequest;
import com.example.web.enrollment.RateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MainIntegrationTest {

    private static HttpServer server;
    private static WebTarget target;

    @BeforeAll
    public static void beforeAll() {
        server = Main.startServer();
        target = ClientBuilder.newBuilder()
            .build()
            .target(Main.BASE_URI);
    }

    @AfterAll
    public static void afterAll() {
        server.shutdownNow();
    }

    private static WebTarget target() {
        return target;
    }

    private static String studentId;
    private static String courseId;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    @Test
    @Order(5)
    void studentCannotRateCourseWithNegativeNumber() {
        final var response = target().path("/enrollments/courses/" + courseId + "/students/" + studentId + "/rate")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(new RateRequest(-1)));

        response.bufferEntity();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(String.class))
            .contains(List.of(
                "\"message\":\"must be greater than or equal to 0\"",
                "\"path\":\"Rating.value\"",
                "\"invalidValue\":\"-1\""
            ));
    }

    @Test
    @Order(6)
    void studentCannotRateCourseNotEnrolled() {
        final var otherCourseId = UUID.randomUUID();
        final var response = target().path("/enrollments/courses/" + otherCourseId + "/students/" + studentId + "/rate")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(new RateRequest(0)));

        response.bufferEntity();

        assertEquals(UnprocessableEntityStatusCode.INSTANCE.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(String.class))
            .contains(List.of("{\"message\":\"Student cannot rating a course they are not enrolled in\"}"));
    }

    private String getLastResourceURI(final URI uri) {
        final var chunks = uri.toString().split("/");

        return chunks[chunks.length - 1];
    }
}
