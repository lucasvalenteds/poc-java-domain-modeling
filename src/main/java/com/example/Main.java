package com.example;

import com.example.infrastructure.configuration.DatabaseFeature;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {
        final ResourceConfig resourceConfig = new ResourceConfig()
            .packages(Main.class.getPackageName())
            .property(DatabaseFeature.DATABASE_URL_PROPERTY, "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
            .property(DatabaseFeature.DATABASE_USER_PROPERTY, "sa")
            .property(DatabaseFeature.DATABASE_PASSWORD_PROPERTY, "password");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
    }

    public static void main(String[] args) throws IOException {
        final var server = Main.startServer();

        LOGGER.info("Application started with endpoints available at {}", Main.BASE_URI);
        LOGGER.info("Hit Ctrl-C to stop it...");

        //noinspection ResultOfMethodCallIgnored
        System.in.read();

        server.shutdownNow();
    }
}
