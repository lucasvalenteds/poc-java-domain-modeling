package com.example.features;

import com.example.domain.enrollment.Enrollment;
import com.example.domain.student.FirstName;
import com.example.domain.student.LastName;
import com.example.domain.student.Student;
import com.example.domain.student.StudentId;
import com.example.infrastructure.validation.Validatable;
import com.example.infrastructure.validation.ValidatorWrapper;
import com.example.persistence.enrollment.EnrollmentRepository;
import com.example.persistence.rating.RatingRepository;
import com.example.persistence.student.StudentRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class StudentManagementDefault implements StudentManagement, Validatable {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentManagementDefault.class);

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final RatingRepository ratingRepository;
    private final Validator validator;

    @Inject
    public StudentManagementDefault(StudentRepository studentRepository,
                                    EnrollmentRepository enrollmentRepository,
                                    RatingRepository ratingRepository,
                                    Validator validator) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.ratingRepository = ratingRepository;
        this.validator = validator;
    }

    @Override
    public Student createStudent(@Valid FirstName firstName, String lastName) {
        final var studentId = ValidatorWrapper.validate(validator, new StudentId(UUID.randomUUID()));
        final var lastName1 = ValidatorWrapper.validate(validator, Optional.ofNullable(lastName).map(LastName::new));
        final var student = new Student(studentId, firstName, lastName1);

        LOGGER.info("Creating student {}", student);

        studentRepository.insert(student);

        LOGGER.info("Student created {}", student);

        return student;
    }

    @Override
    public void removeStudent(@Valid StudentId studentId) {
        LOGGER.info("Removing student {}", studentId.value());

        enrollmentRepository.delete(
            enrollmentRepository.findByStudentId(studentId).stream()
                .map(Enrollment::id)
                .toList()
        );

        LOGGER.info("Removed enrollments of student {}", studentId.value());

        ratingRepository.delete(studentId);

        LOGGER.info("Remove ratings made by student {}", studentId.value());

        studentRepository.delete(studentId);

        LOGGER.info("Student removed {}", studentId.value());
    }

    @Override
    public Student findStudentById(@Valid StudentId studentId) {
        LOGGER.info("Finding student with ID {}", studentId.value());

        return studentRepository.findById(studentId);
    }
}
