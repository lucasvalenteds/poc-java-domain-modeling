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
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Provider
public final class DomainFeature implements Feature {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainFeature.class);

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new ValidatableFeature(Map.ofEntries(
            Map.entry(CourseManagementDefault.class, CourseManagement.class),
            Map.entry(EnrollmentManagementDefault.class, EnrollmentManagement.class),
            Map.entry(RatingManagementDefault.class, RatingManagement.class),
            Map.entry(StudentManagementDefault.class, StudentManagement.class)
        )));
        LOGGER.info("Domain feature initialized");
        return true;
    }
}
