package com.example.features;

import com.example.domain.course.CourseId;
import com.example.domain.enrollment.Enrollment;
import com.example.domain.enrollment.EnrollmentId;
import com.example.domain.student.StudentId;
import com.example.infrastructure.validation.Validatable;
import com.example.infrastructure.validation.ValidatorWrapper;
import com.example.persistence.course.CourseRepository;
import com.example.persistence.enrollment.EnrollmentRepository;
import com.example.persistence.student.StudentRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class EnrollmentManagementDefault implements EnrollmentManagement, Validatable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentManagementDefault.class);

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final Validator validator;

    @Inject
    public EnrollmentManagementDefault(StudentRepository studentRepository,
                                       CourseRepository courseRepository,
                                       EnrollmentRepository enrollmentRepository,
                                       Validator validator) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.validator = validator;
    }

    @Override
    public Enrollment enroll(@Valid CourseId courseId, @Valid StudentId studentId) {
        LOGGER.info("Enrolling {} in {}", studentId.value(), courseId.value());

        final var course = courseRepository.findById(courseId);
        LOGGER.info("Found course {}", course);

        final var student = studentRepository.findById(studentId);
        LOGGER.info("Found student {}", student);

        final var enrollmentId = ValidatorWrapper.validate(validator, new EnrollmentId(UUID.randomUUID()));
        final var enrollment = new Enrollment(enrollmentId, student, course);

        LOGGER.info("Enrolling student {} to course {}", student.id().value(), course.id().value());

        enrollmentRepository.insert(enrollment);

        LOGGER.info("Student enrolled to course: {}", enrollment);

        return enrollment;
    }
}
