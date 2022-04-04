package com.example.features;

import com.example.domain.course.CourseId;
import com.example.domain.course.Rating;
import com.example.domain.student.StudentId;
import com.example.infrastructure.validation.Validatable;
import com.example.persistence.enrollment.EnrollmentRepository;
import com.example.persistence.rating.RatingRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RatingManagementDefault implements RatingManagement, Validatable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingManagementDefault.class);

    private final EnrollmentRepository enrollmentRepository;
    private final RatingRepository ratingRepository;

    @Inject
    public RatingManagementDefault(EnrollmentRepository enrollmentRepository,
                                   RatingRepository ratingRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating rate(@Valid StudentId studentId, @Valid CourseId courseId, @Valid Rating rating) {
        LOGGER.info("Student {} rating course {} as {}", studentId.value(), courseId.value(), rating.value());

        final var studentIsEnrolled = enrollmentRepository.exists(studentId, courseId);
        if (!studentIsEnrolled) {
            throw new IllegalArgumentException("Student cannot rating a course they are not enrolled in");
        }

        ratingRepository.upsert(studentId, courseId, rating);

        LOGGER.info("Student {} rated course {} as {}", studentId.value(), courseId.value(), rating.value());

        return ratingRepository.sum(courseId);
    }
}
