package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.config.ResponseConfig;
import org.example.dto.ResponseDto;
import org.example.interfaces.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to fetch statistics about the library : only available for the admin
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/books")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Retrieve library statistics about books", description = "Fetches statistics on the number of books")
    public ResponseEntity<ResponseDto<String>> getBooksStatistics() {
        int totalBooks = statisticsService.getTotalBooks();
        String message = "The number of books is " + totalBooks;

        return ResponseConfig.success(message);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Retrieve library statistics about categories", description = "Fetches statistics on the number of categories")
    public ResponseEntity<ResponseDto<String>> getCategoriesStatistics() {
        int totalCategories = statisticsService.getTotalCategories();

        String message = "The number of categories is " + totalCategories;

        return ResponseConfig.success(message);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Retrieve library statistics about users", description = "Fetches statistics on the number of users")
    public ResponseEntity<ResponseDto<String>> getUsersStatistics() {
        int totalUsers = statisticsService.getTotalUsers();

        String message = "The number of users is " + totalUsers;

        return ResponseConfig.success(message);
    }

}

