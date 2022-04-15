package com.example;

import com.example.infrastructure.configuration.ApplicationFeature;
import com.example.infrastructure.configuration.DatabaseFeature;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static int getServerPort() {
        return Optional.ofNullable(System.getenv("PORT"))
            .map(Integer::parseInt)
            .orElseGet(() -> 8080);
    }

    public static URI getServerUrl(int serverPort) {
        return URI.create("http://localhost:" + serverPort);
    }

    public static HttpServer startServer(URI uri) {
        final ResourceConfig resourceConfig = new ResourceConfig()
            .property(DatabaseFeature.DATABASE_URL_PROPERTY, "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
            .property(DatabaseFeature.DATABASE_USER_PROPERTY, "sa")
            .property(DatabaseFeature.DATABASE_PASSWORD_PROPERTY, "password")
            .register(DatabaseFeature.class)
            .register(ApplicationFeature.class)
            .packages(Main.class.getPackageName());

        return GrizzlyHttpServerFactory.createHttpServer(uri, resourceConfig);
    }

    public static void main(String[] args) throws IOException {
        final var serverUrl = getServerUrl(getServerPort());
        final var server = Main.startServer(serverUrl);

        LOGGER.info("Application started with endpoints available at {}", serverUrl);
        LOGGER.info("Hit Ctrl-C to stop it...");

        //noinspection ResultOfMethodCallIgnored
        System.in.read();

        server.shutdownNow();
    }

    private Main() {
    }
}
