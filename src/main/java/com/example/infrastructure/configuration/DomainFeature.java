package com.example.infrastructure.configuration;

import com.example.features.CourseManagement;
import com.example.features.CourseManagementDefault;
import com.example.features.EnrollmentManagement;
import com.example.features.EnrollmentManagementDefault;
import com.example.features.RatingManagement;
import com.example.features.RatingManagementDefault;
import com.example.features.StudentManagement;
import com.example.features.StudentManagementDefault;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public final class DomainFeature implements Feature {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainFeature.class);

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new DomainFeature.Binder());
        LOGGER.info("Domain feature initialized");
        return true;
    }

    private static final class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(CourseManagementDefault.class)
                .to(CourseManagement.class)
                .in(Singleton.class);

            bind(EnrollmentManagementDefault.class)
                .to(EnrollmentManagement.class)
                .in(Singleton.class);

            bind(StudentManagementDefault.class)
                .to(StudentManagement.class)
                .in(Singleton.class);

            bind(RatingManagementDefault.class)
                .to(RatingManagement.class)
                .in(Singleton.class);
        }
    }
}
