package com.example.infrastructure.validation;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import java.util.Map;

public final class ValidatableFeature implements Feature {

    private final Map<Class<?>, Class<?>> services;

    public ValidatableFeature(Map<Class<?>, Class<?>> services) {
        this.services = services;
    }

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new ValidatableFeature.Binder(services));
        return true;
    }

    private static final class Binder extends AbstractBinder {

        private final Map<Class<?>, Class<?>> services;

        private Binder(Map<Class<?>, Class<?>> services) {
            this.services = services;
        }

        @Override
        protected void configure() {
            services.forEach((service, contract) ->
                bind(service)
                    .to(contract)
                    .to(Validatable.class)
                    .in(Singleton.class)
            );

            bind(ValidatableInterceptionService.class)
                .to(InterceptionService.class)
                .in(Singleton.class);
        }
    }
}
