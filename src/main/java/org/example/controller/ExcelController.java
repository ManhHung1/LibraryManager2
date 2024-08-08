package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.interfaces.ExcelService;
import org.example.interfaces.UserService;
import org.example.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/excel")
@Tag(name = "Excel Management", description = "Operations related to managing excel import/export")
public class ExcelController {

    private final UserService userService;
    private final ExcelService excelService;

    /**
     * Exports user data to an Excel file and writes it in an Excel excel-file.xlsx at the root of the project
     *
     * @param response the HTTP response to write the Excel file to
     * @throws IOException if an I/O error occurs during file writing
     */
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Export users to an Excel file")
    public void exportUsers(HttpServletResponse response) throws IOException {
        List<User> users = userService.getAllUsers();
        String filePath = "excel-file.xlsx"; // File path

        excelService.exportUsers(users, filePath);

        // Set response headers and write success message
        response.setContentType("text/plain");
        response.getWriter().write("File exported successfully to the path: " + filePath);
    }

    /**
     * Imports user data from an Excel file and updates it into a SQL database
     *
     * @return a ResponseEntity indicating the result of the import operation
     * @throws IOException if an I/O error occurs during file reading
     */
    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Import users from an Excel file")
    public ResponseEntity<String> importUsers() throws IOException {
        String filePath = "excel-file.xlsx"; // Update file path as needed
        List<User> listUsers = excelService.importUsers(filePath);

        if (listUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No users found in the file.");
        }

        for (User user : listUsers) {
            User existingUser = userService.getUserById(user.getId()); // Add this method to your service
            if (existingUser == null) {
                user.setPassword("default-password");
            } else {
                user.setPassword(existingUser.getPassword());
            }
            userService.createUser(user);
        }

        return ResponseEntity.ok("File imported and users saved successfully!");
    }
}
