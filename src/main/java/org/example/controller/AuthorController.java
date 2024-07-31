package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.interfaces.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    @Operation(summary = "Create a new author")
    public ResponseEntity<Author> createAuthor(
            @Parameter(description = "Details of the new author to create", required = true) @RequestBody Author newAuthor) {
        Author createdAuthor = authorService.createAuthor(newAuthor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update an existing author")
    public ResponseEntity<Author> updateAuthor(
            @Parameter(description = "ID of the author to update", required = true) @PathVariable int id,
            @Parameter(description = "Details of the author to update", required = true) @RequestBody Author updatedAuthor) {
        Author author = authorService.updateAuthor(id, updatedAuthor);
        if (author != null) {
            return ResponseEntity.ok(author);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
