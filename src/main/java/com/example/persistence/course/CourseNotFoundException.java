package com.example.persistence.course;

import com.example.domain.course.CourseId;

import java.io.Serial;

public final class CourseNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3951513473009834887L;

    private final CourseId courseId;

    public CourseNotFoundException(CourseId courseId, Throwable throwable) {
        super("Course not found by ID", throwable);
        this.courseId = courseId;
    }

    public CourseId getCourseId() {
        return courseId;
    }
}
