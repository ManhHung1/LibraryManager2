package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to handle user-related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations related to managing users")
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("JWT is working!");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true) @PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a user")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID of the user to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Details of the user to update", required = true) @RequestBody User updatedUser) {
        User updatedUserEntity = userService.updateUser(id, updatedUser);
        if (updatedUserEntity != null) {
            UserDTO updatedUserDTO = convertToDTO(updatedUserEntity);
            return ResponseEntity.ok(updatedUserDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserDTO> createUser(
            @Parameter(description = "Details of the new user to create", required = true) @RequestBody User newUser) {
        User createdUser = userService.createUser(newUser);
        UserDTO createdUserDTO = convertToDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }

    @GetMapping
    @Operation(summary = "Fetch all users in database")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsersDTO();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search-by-username")
    @Operation(summary = "Search users by username")
    public ResponseEntity<List<UserDTO>> searchUsersByUsername(
            @Parameter(description = "Username to search for", required = true) @RequestParam String username) {
        List<UserDTO> users = userService.searchUsersByUsername(username);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search-by-email")
    @Operation(summary = "Search users by email")
    public ResponseEntity<List<UserDTO>> searchUsersByEmail(
            @Parameter(description = "Email to search for", required = true) @RequestParam String email) {
        List<UserDTO> users = userService.searchUsersByEmail(email);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Paginated list of users")
    public ResponseEntity<Page<UserDTO>> getUsersPaginated(
            @Parameter(description = "Page number (zero-based index)", required = true) @RequestParam int page,
            @Parameter(description = "Number of items per page", required = true) @RequestParam int size) {
        Page<UserDTO> userPage = userService.getAllUsersPaginated(page, size);
        return ResponseEntity.ok(userPage);
    }

    @GetMapping("/by-username/{username}")
    @Operation(summary = "Get user by username")
    public ResponseEntity<UserDTO> getUserByUsername(
            @Parameter(description = "Username of the user to retrieve", required = true) @PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getTelephone(), user.getEmail());
    }
}
