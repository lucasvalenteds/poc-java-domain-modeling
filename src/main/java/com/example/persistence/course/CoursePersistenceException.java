package com.example.persistence.course;

import com.example.domain.course.CourseId;

import java.io.Serial;

public final class CoursePersistenceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7247636351755226906L;

    private final CourseId courseId;

    public CoursePersistenceException(CourseId courseId, String message, Throwable throwable) {
        super(message, throwable);
        this.courseId = courseId;
    }

    public CourseId getCourseId() {
        return courseId;
    }
}
