package com.example.web.enrollment;

import com.example.domain.course.CourseId;
import com.example.domain.course.Rating;
import com.example.domain.student.StudentId;
import com.example.features.EnrollmentManagement;
import com.example.features.RatingManagement;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/enrollments")
public final class EnrollmentResource {

    private final EnrollmentManagement enrollmentManagement;
    private final RatingManagement ratingManagement;

    @Inject
    public EnrollmentResource(EnrollmentManagement enrollmentManagement, RatingManagement ratingManagement) {
        this.enrollmentManagement = enrollmentManagement;
        this.ratingManagement = ratingManagement;
    }

    @POST
    @Path("/courses/{courseId}/students/{studentId}/enroll")
    public Response enroll(@PathParam("courseId") UUID courseId,
                           @PathParam("studentId") UUID studentId) {
        final var enrollment = enrollmentManagement.enroll(
            new CourseId(courseId),
            new StudentId(studentId)
        );

        return Response.status(Response.Status.OK)
            .entity(new EnrollResponse(
                enrollment.id().value(),
                enrollment.student().id().value(),
                enrollment.course().id().value()
            ))
            .build();
    }

    @POST
    @Path("/courses/{courseId}/students/{studentId}/rate")
    public Response rate(@PathParam("courseId") UUID courseId,
                         @PathParam("studentId") UUID studentId,
                         RateRequest rateRequest) {
        final var rating = ratingManagement.rate(
            new StudentId(studentId),
            new CourseId(courseId),
            new Rating(rateRequest.rating())
        );

        return Response.status(Response.Status.ACCEPTED)
            .entity(new RateResponse(rating.value()))
            .build();
    }
}
