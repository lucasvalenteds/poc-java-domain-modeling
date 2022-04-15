package com.example;

import com.example.infrastructure.errors.ErrorResponse;
import com.example.infrastructure.errors.UnprocessableEntityStatusCode;
import com.example.web.course.CreateCourseRequest;
import com.example.web.course.FindCourseResponse;
import com.example.web.enrollment.EnrollResponse;
import com.example.web.enrollment.RateRequest;
import com.example.web.enrollment.RateResponse;
import com.example.web.student.CreateStudentRequest;
import com.example.web.student.FindStudentResponse;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MainIntegrationTest {

    private static HttpServer server;
    private static WebTarget target;

    @BeforeAll
    public static void beforeAll() {
        final var serverUrl = Main.getServerUrl(8081);

        server = Main.startServer(serverUrl);
        target = ClientBuilder.newBuilder()
            .build()
            .target(serverUrl);
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

    @Test
    @Order(1)
    void creatingStudent() {
        final var response = target().path("/students")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(new CreateStudentRequest("John", "Smith")));

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
            .post(Entity.json(new CreateCourseRequest("ALG1001", "Introduction to Algorithms")));

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
    void studentCourseRatingUpdate() {
        final var response = target().path("/enrollments/courses/" + courseId + "/students/" + studentId + "/rate")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(new RateRequest(5)));

        response.bufferEntity();

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());

        final var rateResponse = response.readEntity(RateResponse.class);
        assertNotNull(rateResponse);
        assertEquals(5, rateResponse.rating());
    }

    @Test
    @Order(6)
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
    @Order(7)
    void studentCannotRateCourseNotEnrolled() {
        final var otherCourseId = UUID.randomUUID();
        final var response = target().path("/enrollments/courses/" + otherCourseId + "/students/" + studentId + "/rate")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(new RateRequest(0)));

        response.bufferEntity();

        assertEquals(UnprocessableEntityStatusCode.INSTANCE.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(ErrorResponse.class))
            .matches(error -> error.message().equals("Student cannot rating a course they are not enrolled in"));
    }

    @Test
    @Order(8)
    void findingStudentId() {
        final var response = target().path("/students/" + studentId)
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .get();

        response.bufferEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        final var responseBody = response.readEntity(FindStudentResponse.class);
        assertEquals(studentId, responseBody.id());
        assertEquals("John", responseBody.firstName());
        assertEquals("Smith", responseBody.lastName());
    }

    @Test
    @Order(9)
    void findingCourseById() {
        final var response = target().path("/courses/" + courseId)
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .get();

        response.bufferEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        final var responseBody = response.readEntity(FindCourseResponse.class);
        assertEquals(courseId, responseBody.id());
        assertEquals("ALG1001", responseBody.code());
        assertEquals("Introduction to Algorithms", responseBody.title());
        assertEquals(5, responseBody.rating());
    }

    @Test
    @Order(10)
    void removingStudentById() {
        final var response = target().path("/students/" + studentId)
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .delete();

        response.bufferEntity();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(11)
    void removingCourseById() {
        final var response = target().path("/courses/" + courseId)
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .delete();

        response.bufferEntity();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(12)
    void notFindingStudentById() {
        final var response = target().path("/students/" + studentId)
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .get();

        response.bufferEntity();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(13)
    void notFindingCourseById() {
        final var response = target().path("/courses/" + courseId)
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .get();

        response.bufferEntity();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(14)
    void creatingStudentWithoutLastName() {
        final var response = target().path("/students")
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .post(Entity.json(new CreateStudentRequest("John", null)));

        response.bufferEntity();

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());

        final var studentId = getLastResourceURI(response.getLocation());
        final var response1 = target().path("/students/" + studentId)
            .request()
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .get();

        response1.bufferEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());

        final var findStudentResponse = response1.readEntity(FindStudentResponse.class);
        assertNotNull(findStudentResponse);
        assertEquals(studentId, findStudentResponse.id());
        assertEquals("John", findStudentResponse.firstName());
        assertNull(findStudentResponse.lastName());
    }

    private String getLastResourceURI(final URI uri) {
        final var chunks = uri.toString().split("/");

        return chunks[chunks.length - 1];
    }
}
