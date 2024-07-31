package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for application-specific beans and settings.
 */
@Configuration
public class AppConfig {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AppConfig.class);

    /**
     * Bean definition for PasswordEncoder.
     *
     * @return a BCryptPasswordEncoder instance used for encoding passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating PasswordEncoder bean using BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }
}
