package com.example.infrastructure.validation;

import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Parameter;

public final class ValidatableMethodInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatableMethodInterceptor.class);

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        LOGGER.info("Intercepting method {}", invocation.getMethod().getName());
        return intercept(invocation, invocation.getMethod().getParameters());
    }

    private Object intercept(final Invocation invocation, final Parameter[] parameters) throws Throwable {
        final var arguments = invocation.getArguments();

        for (var index = 0; index < arguments.length; index++) {
            if (parameters[index].isAnnotationPresent(Valid.class)) {
                ValidatorWrapper.validate(validator, arguments[index]);
            }
        }

        return invocation.proceed();
    }
}
