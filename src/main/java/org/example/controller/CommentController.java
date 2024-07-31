package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.CommentDTO;
import org.example.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Operations related to managing comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    @Operation(summary = "Retrieve all comments", description = "Fetches a list of all comments")
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a comment by ID", description = "Fetches details of a comment by its ID")
    public ResponseEntity<CommentDTO> getCommentById(
            @Parameter(description = "ID of the comment to retrieve", required = true) @PathVariable int id
    ) {
        CommentDTO comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    @Operation(summary = "Create a new comment", description = "Adds a new comment")
    public ResponseEntity<CommentDTO> createComment(
            @Parameter(description = "Details of the comment to create", required = true) @RequestBody CommentDTO commentDTO
    ) {
        CommentDTO createdComment = commentService.saveComment(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing comment", description = "Updates the details of an existing comment by its ID")
    public ResponseEntity<CommentDTO> updateComment(
            @Parameter(description = "ID of the comment to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated details of the comment", required = true) @RequestBody CommentDTO commentDTO
    ) {
        commentDTO.setId(id);
        CommentDTO updatedComment = commentService.saveComment(commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a comment", description = "Deletes a comment by its ID")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID of the comment to delete", required = true) @PathVariable int id
    ) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
