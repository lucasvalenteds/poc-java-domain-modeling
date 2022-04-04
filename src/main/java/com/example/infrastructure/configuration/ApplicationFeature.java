package com.example.infrastructure.configuration;

import com.example.features.CourseManagement;
import com.example.features.CourseManagementDefault;
import com.example.features.EnrollmentManagement;
import com.example.features.EnrollmentManagementDefault;
import com.example.features.RatingManagement;
import com.example.features.RatingManagementDefault;
import com.example.features.StudentManagement;
import com.example.features.StudentManagementDefault;
import com.example.infrastructure.validation.ValidatableFeature;
import com.example.persistence.course.CourseRepository;
import com.example.persistence.course.CourseRepositoryDefault;
import com.example.persistence.enrollment.EnrollmentRepository;
import com.example.persistence.enrollment.EnrollmentRepositoryDefault;
import com.example.persistence.rating.RatingRepository;
import com.example.persistence.rating.RatingRepositoryDefault;
import com.example.persistence.student.StudentRepository;
import com.example.persistence.student.StudentRepositoryDefault;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class ApplicationFeature implements Feature {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFeature.class);

    @Override
    public boolean configure(FeatureContext context) {
        context
            .register(LoggingFeature.class)
            .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, "SEVERE")
            .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY, LoggingFeature.Verbosity.PAYLOAD_ANY)
            .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
            .property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true)
            .register(new ValidatableFeature(Map.ofEntries(
                Map.entry(CourseRepositoryDefault.class, CourseRepository.class),
                Map.entry(EnrollmentRepositoryDefault.class, EnrollmentRepository.class),
                Map.entry(RatingRepositoryDefault.class, RatingRepository.class),
                Map.entry(StudentRepositoryDefault.class, StudentRepository.class),
                Map.entry(CourseManagementDefault.class, CourseManagement.class),
                Map.entry(EnrollmentManagementDefault.class, EnrollmentManagement.class),
                Map.entry(RatingManagementDefault.class, RatingManagement.class),
                Map.entry(StudentManagementDefault.class, StudentManagement.class)
            )));

        LOGGER.info("Domain feature initialized");

        return true;
    }
}
