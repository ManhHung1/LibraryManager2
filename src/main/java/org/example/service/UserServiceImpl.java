package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.interfaces.UserService;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsersDTO() {
        logger.info("Retrieving all users.");
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.debug("Retrieved {} users.", userDTOs.size());
        return userDTOs;
    }


    // For Excel export only
    @Override
    public List<User> getAllUsers() {
        logger.info("Retrieving all users as entities.");
        List<User> users = userRepository.findAll();
        logger.debug("Retrieved {} users.", users.size());
        return users;
    }

    @Override
    public User createUser(User newUser) {
        logger.info("Creating user with username: {}", newUser.getUsername());
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        User savedUser = userRepository.save(newUser);
        logger.debug("User with username: {} created successfully.", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public void deleteUser(Integer id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        logger.debug("User with ID: {} deleted successfully.", id);
    }

    @Override
    public User updateUser(Integer id, User updatedUser) {
        logger.info("Updating user with ID: {}", id);
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setTelephone(updatedUser.getTelephone());
            existingUser.setEmail(updatedUser.getEmail());

            User savedUser = userRepository.save(existingUser);
            logger.debug("User with ID: {} updated successfully.", id);
            return savedUser;
        } else {
            logger.warn("User with ID: {} not found.", id);
            return null; // or throw an exception if preferred
        }
    }

    @Override
    public List<UserDTO> searchUsersByUsername(String username) {
        logger.info("Searching for users with username containing: {}", username);
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.debug("Found {} users with username containing: {}", userDTOs.size(), username);
        return userDTOs;
    }

    @Override
    public List<UserDTO> searchUsersByEmail(String email) {
        logger.info("Searching for users with email containing: {}", email);
        List<User> users = userRepository.findByEmailContainingIgnoreCase(email);
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.debug("Found {} users with email containing: {}", userDTOs.size(), email);
        return userDTOs;
    }

    @Override
    public Page<UserDTO> getAllUsersPaginated(int page, int size) {
        logger.info("Retrieving users page {} with size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userRepository.findAll(pageable);
        Page<UserDTO> userDTOPage = usersPage.map(this::convertToDTO);
        logger.debug("Retrieved page {} with {} users.", page, userDTOPage.getNumberOfElements());
        return userDTOPage;
    }

    @Override
    public User getUserById(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElse(null);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findOptByUsername(username);

        return userOpt.map(this::convertToDTO).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getTelephone(), user.getEmail());
    }
}
