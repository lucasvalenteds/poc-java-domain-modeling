package com.example.domain.enrollment;

import com.example.domain.course.CourseId;
import com.example.domain.student.StudentId;
import jakarta.validation.Valid;

public record Enrollment(
    @Valid EnrollmentId id,
    @Valid StudentId studentId,
    @Valid CourseId courseId
) {
}