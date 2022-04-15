package com.example.infrastructure.configuration;

import jakarta.ws.rs.RuntimeType;
import org.glassfish.jersey.model.ContractProvider;
import org.glassfish.jersey.model.internal.CommonConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class DatabaseFeatureTest {

    @Test
    void handlingDatabaseMigrationErrors() {
        final var feature = new DatabaseFeature();
        final var featureContext = new CommonConfig(RuntimeType.SERVER, ContractProvider::isNameBound);

        final var configured = feature.configure(featureContext);

        assertFalse(configured);
    }
}