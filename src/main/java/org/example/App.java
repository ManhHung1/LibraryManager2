package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Main application class to bootstrap and launch the Spring Boot application.
 */

@SpringBootApplication
@EnableMethodSecurity
@EnableScheduling
public class App {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Starting the Spring Boot application...");
        SpringApplication.run(App.class, args);
        logger.info("Spring Boot application started successfully.");
    }
}
