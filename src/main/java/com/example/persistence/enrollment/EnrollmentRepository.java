package com.example.persistence.enrollment;

import com.example.domain.course.CourseId;
import com.example.domain.enrollment.Enrollment;
import com.example.domain.enrollment.EnrollmentId;
import com.example.domain.student.StudentId;

import java.util.List;

public interface EnrollmentRepository {

    void insert(Enrollment enrollment) throws EnrollmentPersistenceException;

    void delete(List<EnrollmentId> enrollmentId) throws EnrollmentPersistenceException;

    boolean exists(StudentId studentId, CourseId courseId) throws EnrollmentPersistenceException;

    List<Enrollment> findByStudentId(StudentId studentId) throws EnrollmentPersistenceException;

    List<Enrollment> findByCourseId(CourseId courseId) throws EnrollmentPersistenceException;
}
