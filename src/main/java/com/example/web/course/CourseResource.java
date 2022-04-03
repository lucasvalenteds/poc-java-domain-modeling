package com.example.web.course;

import com.example.domain.course.Code;
import com.example.domain.course.CourseId;
import com.example.domain.course.Title;
import com.example.features.CourseManagement;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.UUID;

@Path("/courses")
public final class CourseResource {

    private final CourseManagement courseManagement;

    @Inject
    public CourseResource(CourseManagement courseManagement) {
        this.courseManagement = courseManagement;
    }

    @POST
    public Response createCourse(@Context UriInfo uriInfo, CreateCourseRequest createCourseRequest) {
        final var course = courseManagement.createCourse(
            new Code(createCourseRequest.code()),
            new Title(createCourseRequest.title())
        );

        return Response.status(Response.Status.CREATED)
            .location(uriInfo.getAbsolutePathBuilder()
                .path(course.id().value().toString())
                .build())
            .build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeCourse(@PathParam("id") UUID id) {
        courseManagement.removeCourse(new CourseId(id));

        return Response.status(Response.Status.NO_CONTENT)
            .build();
    }

    @GET
    @Path("/{id}")
    public Response findCourse(@PathParam("id") UUID id) {
        final var course = courseManagement.findCourseById(new CourseId(id));

        return Response.status(Response.Status.OK)
            .entity(new FindCourseResponse(
                course.id().value().toString(),
                course.code().value(),
                course.title().value(),
                course.rating().value()
            ))
            .build();
    }
}
