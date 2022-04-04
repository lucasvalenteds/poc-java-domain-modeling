package com.example.web.student;

import com.example.domain.student.FirstName;
import com.example.domain.student.LastName;
import com.example.domain.student.StudentId;
import com.example.features.StudentManagement;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Path("/students")
public final class StudentResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentResource.class);

    private final StudentManagement studentManagement;

    @Inject
    public StudentResource(StudentManagement studentManagement) {
        this.studentManagement = studentManagement;
    }

    @POST
    public Response createStudent(@Context UriInfo uriInfo, CreateStudentRequest createStudentRequest) {
        LOGGER.info("Create student: {}", createStudentRequest);
        final var student = studentManagement.createStudent(
            new FirstName(createStudentRequest.firstName()),
            createStudentRequest.lastName()
        );

        return Response.status(Response.Status.CREATED)
            .location(uriInfo.getAbsolutePathBuilder()
                .path(student.id().value().toString())
                .build())
            .build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeStudent(@PathParam("id") UUID id) {
        studentManagement.removeStudent(new StudentId(id));

        return Response.status(Response.Status.NO_CONTENT)
            .build();
    }

    @GET
    @Path("/{id}")
    public Response findStudent(@PathParam("id") UUID id) {
        final var student = studentManagement.findStudentById(new StudentId(id));

        return Response.status(Response.Status.OK)
            .entity(new FindStudentResponse(
                student.id().value().toString(),
                student.firstName().value(),
                student.lastName()
                    .map(LastName::value)
                    .orElse(null)
            ))
            .build();
    }
}
