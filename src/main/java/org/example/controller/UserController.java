package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.config.ResponseConfig;
import org.example.dto.UserDTO;
import org.example.dto.ResponseDto;
import org.example.model.User;
import org.example.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to handle user-related admin requests (add/delete users for example).
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations related to managing users")
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<ResponseDto<String>> test() {
        return ResponseConfig.success("JWT is working!");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a user")
    public ResponseEntity<ResponseDto<Void>> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true) @PathVariable Integer id) {
        boolean success = userService.deleteUser(id);
        return ResponseConfig.successDelete(null, success);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a user")
    public ResponseEntity<ResponseDto<UserDTO>> updateUser(
            @Parameter(description = "ID of the user to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Details of the user to update", required = true) @RequestBody User updatedUser) {
        User updatedUserEntity = userService.updateUser(id, updatedUser);
        if (updatedUserEntity != null) {
            UserDTO updatedUserDTO = convertToDTO(updatedUserEntity);
            return ResponseConfig.success(updatedUserDTO);
        } else {
            return ResponseConfig.error(HttpStatus.NOT_FOUND, "01", "User not found");
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new user")
    public ResponseEntity<ResponseDto<UserDTO>> createUser(
            @Parameter(description = "Details of the new user to create", required = true) @RequestBody User newUser) {
        User createdUser = userService.createUser(newUser);
        UserDTO createdUserDTO = convertToDTO(createdUser);
        return ResponseConfig.success(createdUserDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Fetch all users in database")
    public ResponseEntity<ResponseDto<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsersDTO();
        return ResponseConfig.success(users);
    }

    @GetMapping("/search-by-username")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Search users by username")
    public ResponseEntity<ResponseDto<List<UserDTO>>> searchUsersByUsername(
            @Parameter(description = "Username to search for", required = true) @RequestParam String username) {
        List<UserDTO> users = userService.searchUsersByUsername(username);
        return ResponseConfig.success(users);
    }

    @GetMapping("/search-by-email")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Search users by email")
    public ResponseEntity<ResponseDto<List<UserDTO>>> searchUsersByEmail(
            @Parameter(description = "Email to search for", required = true) @RequestParam String email) {
        List<UserDTO> users = userService.searchUsersByEmail(email);
        return ResponseConfig.success(users);
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Paginated list of users")
    public ResponseEntity<ResponseDto<Page<UserDTO>>> getUsersPaginated(
            @Parameter(description = "Page number (zero-based index)", required = true) @RequestParam int page,
            @Parameter(description = "Number of items per page", required = true) @RequestParam int size) {
        Page<UserDTO> userPage = userService.getAllUsersPaginated(page, size);
        return ResponseConfig.success(userPage);
    }

    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by username")
    public ResponseEntity<ResponseDto<UserDTO>> getUserByUsername(
            @Parameter(description = "Username of the user to retrieve", required = true) @PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseConfig.success(user);
        } else {
            return ResponseConfig.error(HttpStatus.NOT_FOUND, "01", "User not found");
        }
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getTelephone(), user.getEmail());
    }
}
