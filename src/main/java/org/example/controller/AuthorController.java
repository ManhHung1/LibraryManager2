package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.config.ResponseConfig;
import org.example.dto.ResponseDto;
import org.example.model.Author;
import org.example.interfaces.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle author-related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
@Tag(name = "Author Management", description = "Operations related to managing authors")
public class AuthorController {


    private final AuthorService authorService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('MANAGER')") // Restrict access to MANAGER role
    @Operation(summary = "Create a new author")
    public ResponseEntity<ResponseDto<Author>> createAuthor(
            @Parameter(description = "Details of the new author to create", required = true) @RequestBody Author newAuthor) {
        Author createdAuthor = authorService.createAuthor(newAuthor);
        return ResponseConfig.success(createdAuthor);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('MANAGER')") // Restrict access to MANAGER role
    @Operation(summary = "Update an existing author")
    public ResponseEntity<ResponseDto<Author>> updateAuthor(
            @Parameter(description = "ID of the author to update", required = true) @PathVariable int id,
            @Parameter(description = "Details of the author to update", required = true) @RequestBody Author updatedAuthor) {
        Author author = authorService.updateAuthor(id, updatedAuthor);
        if (author != null) {
            return ResponseConfig.success(author);
        } else {
            return ResponseConfig.error(HttpStatus.NOT_FOUND, "01", "Author not found");
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('MANAGER')") // Restrict access to MANAGER role
    @Operation(summary = "Delete an author")
    public ResponseEntity<ResponseDto<Void>> deleteAuthor(
            @Parameter(description = "ID of the author to delete", required = true) @PathVariable int id) {
        boolean success = authorService.deleteAuthor(id);
        if (success) {
            return ResponseConfig.successDelete(null, success);
        } else {
            return ResponseConfig.error(HttpStatus.NOT_FOUND, "01", "Author not found");
        }
    }

}
