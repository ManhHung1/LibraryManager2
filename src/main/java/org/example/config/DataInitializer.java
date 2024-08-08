package org.example.config;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.example.model.Role;
import org.example.repository.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.existsByUsername("library_admin")) {
            User admin = new User();
            admin.setUsername("library_admin");
            admin.setPassword(passwordEncoder.encode("Admin1234")); // Change this to a secure password
            admin.setEmail("testadmin@library.com");
            admin.setTelephone("1234567890");
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
            admin.setRole(adminRole);
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("library_librarian")) {
            User librarian = new User();
            librarian.setUsername("library_librarian");
            librarian.setPassword(passwordEncoder.encode("Librarian1234")); // Set a secure password
            librarian.setEmail("testlibrarian@library.com");
            librarian.setTelephone("0987654321");
            Role librarianRole = new Role();
            librarianRole.setName("ROLE_LIBRARIAN");
            roleRepository.save(librarianRole);
            librarian.setRole(librarianRole);
            userRepository.save(librarian);
        }

        if (!userRepository.existsByUsername("library_manager")) {
            User manager = new User();
            manager.setUsername("library_manager");
            manager.setPassword(passwordEncoder.encode("Manager1234")); // Set a secure password
            manager.setEmail("testmanager@library.com");
            manager.setTelephone("1122334455");
            Role managerRole = new Role();
            managerRole.setName("ROLE_MANAGER");
            roleRepository.save(managerRole);

            manager.setRole(managerRole);
            userRepository.save(manager);
        }

        if (!userRepository.existsByUsername("MomoNagi")){
            User user = new User();
            user.setUsername("MomoNagi");
            user.setPassword((passwordEncoder.encode("MomoNagi")));
            user.setEmail("lemanhhung230203@gmail.com");
            user.setTelephone("0132309311");
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            user.setRole(userRole);
            userRepository.save(user);
        }
    }
}

