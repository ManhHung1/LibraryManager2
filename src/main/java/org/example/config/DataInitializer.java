package org.example.config;

import lombok.AllArgsConstructor;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.example.model.Role;
import org.example.repository.RoleRepository;

@AllArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_LIBRARIAN");
        createRoleIfNotExists("ROLE_MANAGER");
        createRoleIfNotExists("ROLE_USER");

        createUserIfNotExists("library_admin", "Admin1234", "testadmin@library.com", "1234567890", "ROLE_ADMIN");
        createUserIfNotExists("library_librarian", "Librarian1234", "testlibrarian@library.com", "0987654321", "ROLE_LIBRARIAN");
        createUserIfNotExists("library_manager", "Manager1234", "testmanager@library.com", "1122334455", "ROLE_MANAGER");
        createUserIfNotExists("MomoNagi", "MomoNagi", "lemanhhung230203@gmail.com", "0132309311", "ROLE_USER");
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName) == null) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }

    private void createUserIfNotExists(String username, String password, String email, String telephone, String roleName) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setTelephone(telephone);

            Role role = roleRepository.findByName(roleName);
            user.setRole(role);

            userRepository.save(user);
        }
    }
}
