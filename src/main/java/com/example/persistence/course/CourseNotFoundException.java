package com.example.persistence.course;

import com.example.domain.course.CourseId;
import com.example.infrastructure.errors.ResourceNotFoundException;

import java.io.Serial;

public final class CourseNotFoundException extends ResourceNotFoundException {

    @Serial
    private static final long serialVersionUID = 3951513473009834887L;

    private final transient CourseId courseId;

    public CourseNotFoundException(CourseId courseId, Throwable throwable) {
        super("Course not found by ID", throwable);
        this.courseId = courseId;
    }

    public CourseId getCourseId() {
        return courseId;
    }
}
