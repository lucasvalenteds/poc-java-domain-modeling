package com.example.testing;

import com.example.domain.enrollment.Enrollment;
import com.example.domain.enrollment.EnrollmentId;
import com.example.infrastructure.validation.ValidatorWrapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.UUID;

public final class EnrollmentTestBuilder {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    private EnrollmentTestBuilder() {
    }

    public static EnrollmentId id() {
        return ValidatorWrapper.validate(VALIDATOR, new EnrollmentId(UUID.randomUUID()));
    }

    public static Enrollment enrollment() {
        return ValidatorWrapper.validate(VALIDATOR, new Enrollment(
            EnrollmentTestBuilder.id(),
            StudentTestBuilder.student(),
            CourseTestBuilder.course()
        ));
    }
}
