package com.example.domain.enrollment;

import com.example.domain.course.Course;
import com.example.domain.student.Student;
import jakarta.validation.Valid;

public record Enrollment(
    @Valid EnrollmentId id,
    @Valid Student student,
    @Valid Course course
) {
}