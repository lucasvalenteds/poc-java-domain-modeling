package com.example.testing;

import com.example.domain.course.Code;
import com.example.domain.course.Course;
import com.example.domain.course.CourseId;
import com.example.domain.course.Rating;
import com.example.domain.course.Title;
import com.example.infrastructure.validation.ValidatorWrapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.UUID;

public final class CourseTestBuilder {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    private CourseTestBuilder() {
    }

    public static CourseId id() {
        return ValidatorWrapper.validate(VALIDATOR, new CourseId(UUID.randomUUID()));
    }

    public static Code code() {
        return ValidatorWrapper.validate(VALIDATOR, new Code("ALG1001"));
    }

    public static Title title() {
        return ValidatorWrapper.validate(VALIDATOR, new Title("Introduction to Algorithms"));
    }

    public static Rating rating() {
        return ValidatorWrapper.validate(VALIDATOR, new Rating(5));
    }

    public static Course course() {
        return ValidatorWrapper.validate(VALIDATOR, new Course(
            CourseTestBuilder.id(),
            CourseTestBuilder.code(),
            CourseTestBuilder.title(),
            CourseTestBuilder.rating()
        ));
    }
}
