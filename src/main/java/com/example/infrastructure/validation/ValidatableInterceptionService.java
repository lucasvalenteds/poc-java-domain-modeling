package com.example.infrastructure.validation;

import jakarta.validation.Valid;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

public final class ValidatableInterceptionService implements InterceptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatableInterceptionService.class);

    private final static MethodInterceptor METHOD_INTERCEPTOR = new ValidatableMethodInterceptor();
    private final static List<MethodInterceptor> METHOD_INTERCEPTORS = Collections.singletonList(METHOD_INTERCEPTOR);

    @Override
    public Filter getDescriptorFilter() {
        return BuilderHelper.createContractFilter(Validatable.class.getCanonicalName());
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors(Method method) {
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Valid.class)) {
                LOGGER.info("Intercepting {} annotated with @Valid", parameter);
                return METHOD_INTERCEPTORS;
            }
        }
        return null;
    }

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
        return null;
    }
}
