package com.example.infrastructure.configuration;

import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public final class WebFeature implements Feature {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebFeature.class);

    @Override
    public boolean configure(FeatureContext context) {
        context.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        context.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);

        context.register(LoggingFeature.class);
        context.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, "SEVERE");
        context.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY, LoggingFeature.Verbosity.PAYLOAD_ANY);

        LOGGER.info("Web configuration initialized");

        return true;
    }
}
