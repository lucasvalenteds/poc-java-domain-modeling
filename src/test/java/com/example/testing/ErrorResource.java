package com.example.testing;

import com.example.domain.course.CourseId;
import com.example.domain.student.StudentId;
import com.example.infrastructure.errors.BusinessException;
import com.example.persistence.course.CourseNotFoundException;
import com.example.persistence.student.StudentPersistenceException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.UUID;

@Path("/errors")
public final class ErrorResource {

    private final UUID id = UUID.randomUUID();

    @GET
    @Path("/business")
    public void businessException() {
        throw new BusinessException("Some error");
    }

    @GET
    @Path("/resource-not-found")
    public void resourceNotFoundException() {
        throw new CourseNotFoundException(new CourseId(id), new RuntimeException("Cause"));
    }

    @GET
    @Path("/persistence")
    public void persistenceException() {
        throw new StudentPersistenceException(new StudentId(id), "Message", new RuntimeException("Cause"));
    }
}
