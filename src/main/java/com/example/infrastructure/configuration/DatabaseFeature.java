package com.example.infrastructure.configuration;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Optional;

public final class DatabaseFeature implements Feature {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseFeature.class);

    private static final String DATABASE_URL_VARIABLE = "DATABASE_URL";
    private static final String DATABASE_USER_VARIABLE = "DATABASE_USER";
    private static final String DATABASE_PASSWORD_VARIABLE = "DATABASE_PASSWORD";

    public static final String DATABASE_URL_PROPERTY = "database.url";
    public static final String DATABASE_USER_PROPERTY = "database.user";
    public static final String DATABASE_PASSWORD_PROPERTY = "database.password";

    public static final String DATABASE_MIGRATIONS_LOCATION = "database.migrations";
    private static final String DATABASE_MIGRATIONS_LOCATION_DEFAULT = "db/migrations";

    @Override
    public boolean configure(FeatureContext context) {
        final var configuration = context.getConfiguration();
        final var dataSource = this.createDataSource(configuration);
        final var flyway = this.createFlyway(configuration, dataSource);

        context.register(new DatabaseFeature.Binder(dataSource, flyway));

        try {
            final var migrateResult = flyway.migrate();
            LOGGER.info("Database connection established ad migrations {} executed", migrateResult.migrationsExecuted);
            return true;
        } catch (FlywayException exception) {
            LOGGER.error("Error configuring the database and migrations", exception);
            return false;
        }
    }

    private DataSource createDataSource(Configuration configuration) {
        final var dataSource = new JdbcDataSource();

        dataSource.setURL(
            Optional.ofNullable(System.getenv(DATABASE_URL_VARIABLE))
                .orElseGet(() -> (String) configuration.getProperty(DATABASE_URL_PROPERTY))
        );
        dataSource.setUser(
            Optional.ofNullable(System.getenv(DATABASE_USER_VARIABLE))
                .orElseGet(() -> (String) configuration.getProperty(DATABASE_USER_PROPERTY))
        );
        dataSource.setPassword(
            Optional.ofNullable(System.getenv(DATABASE_PASSWORD_VARIABLE))
                .orElseGet(() -> (String) configuration.getProperty(DATABASE_PASSWORD_PROPERTY))
        );

        return dataSource;
    }

    private Flyway createFlyway(Configuration configuration, DataSource dataSource) {
        return Flyway.configure()
            .dataSource(dataSource)
            .locations(
                Optional.ofNullable((String) configuration.getProperty(DATABASE_MIGRATIONS_LOCATION))
                    .orElseGet(() -> DATABASE_MIGRATIONS_LOCATION_DEFAULT)
            )
            .load();
    }

    private static final class Binder extends AbstractBinder {

        private final DataSource dataSource;
        private final Flyway flyway;

        private Binder(DataSource dataSource, Flyway flyway) {
            this.dataSource = dataSource;
            this.flyway = flyway;
        }

        @Override
        protected void configure() {
            bind(this.dataSource)
                .to(DataSource.class)
                .in(Singleton.class);

            bind(this.flyway)
                .to(Flyway.class)
                .in(Singleton.class);
        }
    }
}
