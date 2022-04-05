package com.example.testing;

import com.example.domain.student.FirstName;
import com.example.domain.student.LastName;
import com.example.domain.student.Student;
import com.example.domain.student.StudentId;
import com.example.infrastructure.validation.ValidatorWrapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Optional;
import java.util.UUID;

public final class StudentTestBuilder {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    private StudentTestBuilder() {
    }

    public static StudentId id() {
        return ValidatorWrapper.validate(VALIDATOR, new StudentId(UUID.randomUUID()));
    }

    public static FirstName firstName() {
        return ValidatorWrapper.validate(VALIDATOR, new FirstName("John"));
    }

    public static LastName lastName() {
        return ValidatorWrapper.validate(VALIDATOR, new LastName("Smith"));
    }

    public static Student student() {
        return ValidatorWrapper.validate(VALIDATOR, new Student(
            StudentTestBuilder.id(),
            StudentTestBuilder.firstName(),
            Optional.of(StudentTestBuilder.lastName())
        ));
    }
}
