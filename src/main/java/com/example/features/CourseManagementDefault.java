package com.example.features;

import com.example.domain.course.Code;
import com.example.domain.course.Course;
import com.example.domain.course.CourseId;
import com.example.domain.course.Rating;
import com.example.domain.course.Title;
import com.example.domain.enrollment.Enrollment;
import com.example.infrastructure.validation.ValidatorWrapper;
import com.example.persistence.course.CourseRepository;
import com.example.persistence.enrollment.EnrollmentRepository;
import com.example.persistence.rating.RatingRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class CourseManagementDefault implements CourseManagement {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseManagementDefault.class);

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final RatingRepository ratingRepository;
    private final Validator validator;

    @Inject
    public CourseManagementDefault(CourseRepository courseRepository,
                                   EnrollmentRepository enrollmentRepository,
                                   RatingRepository ratingRepository,
                                   Validator validator) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.ratingRepository = ratingRepository;
        this.validator = validator;
    }

    @Override
    public Course createCourse(@Valid Code code, @Valid Title title) {
        final var courseId = ValidatorWrapper.validate(validator, new CourseId(UUID.randomUUID()));
        final var rating = ValidatorWrapper.validate(validator, new Rating(0));
        final var course = new Course(courseId, code, title, rating);

        LOGGER.info("Creating course {}", course);

        courseRepository.insert(course);

        LOGGER.info("Course created {}", course);

        return course;
    }

    @Override
    public void removeCourse(@Valid CourseId courseId) {
        LOGGER.info("Removing course {}", courseId.value());

        enrollmentRepository.delete(
            enrollmentRepository.findByCourseId(courseId).stream()
                .map(Enrollment::id)
                .toList()
        );

        LOGGER.info("Removed enrollments of course {}", courseId.value());

        ratingRepository.delete(courseId);

        LOGGER.info("Removed ratings of course {}", courseId.value());

        courseRepository.delete(courseId);

        LOGGER.info("Course removed {}", courseId.value());
    }

    @Override
    public Course findCourseById(@Valid CourseId courseId) {
        LOGGER.info("Finding course with ID {}", courseId.value());

        return courseRepository.findById(courseId);
    }
}
