package com.example.features;

import com.example.domain.course.CourseId;
import com.example.domain.enrollment.Enrollment;
import com.example.domain.student.StudentId;

public interface EnrollmentManagement {

    Enrollment enroll(CourseId courseId, StudentId studentId);
}
