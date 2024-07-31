package org.example.config;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
            User admin = new User();
            admin.setUsername("test");
            admin.setPassword(passwordEncoder.encode("test123")); // Change this to a secure password
            admin.setEmail("test@library.com");
            admin.setTelephone("1234567890");
            userRepository.save(admin);
    }
}
