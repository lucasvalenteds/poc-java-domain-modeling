package com.example.infrastructure.configuration;

import com.example.persistence.course.CourseRepository;
import com.example.persistence.course.CourseRepositoryDefault;
import com.example.persistence.enrollment.EnrollmentRepository;
import com.example.persistence.enrollment.EnrollmentRepositoryDefault;
import com.example.persistence.rating.RatingRepository;
import com.example.persistence.rating.RatingRepositoryDefault;
import com.example.persistence.student.StudentRepository;
import com.example.persistence.student.StudentRepositoryDefault;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public final class PersistenceFeature implements Feature {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceFeature.class);

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new PersistenceFeature.Binder());
        LOGGER.info("Persistence feature initialized");
        return true;
    }

    private static final class Binder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(CourseRepositoryDefault.class)
                .to(CourseRepository.class)
                .in(Singleton.class);

            bind(EnrollmentRepositoryDefault.class)
                .to(EnrollmentRepository.class)
                .in(Singleton.class);

            bind(RatingRepositoryDefault.class)
                .to(RatingRepository.class)
                .in(Singleton.class);

            bind(StudentRepositoryDefault.class)
                .to(StudentRepository.class)
                .in(Singleton.class);
        }
    }
}
