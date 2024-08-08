package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.config.ResponseConfig;
import org.example.dto.CommentDTO;
import org.example.dto.ResponseDto;
import org.example.interfaces.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Operations related to managing comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "Retrieve all comments", description = "Fetches a list of all comments")
    public ResponseEntity<ResponseDto<List<CommentDTO>>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseConfig.success(comments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a comment by ID", description = "Fetches details of a comment by its ID")
    public ResponseEntity<ResponseDto<CommentDTO>> getCommentById(
            @Parameter(description = "ID of the comment to retrieve", required = true) @PathVariable int id
    ) {
        CommentDTO comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseConfig.success(comment);
        } else {
            return ResponseConfig.error(HttpStatus.NOT_FOUND, "01", "Comment not found");
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a new comment", description = "Adds a new comment")
    public ResponseEntity<ResponseDto<CommentDTO>> createComment(
            @Parameter(description = "Details of the comment to create", required = true) @RequestBody CommentDTO commentDTO
    ) {
        CommentDTO createdComment = commentService.saveComment(commentDTO);
        return ResponseConfig.success(createdComment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update an existing comment", description = "Updates the details of an existing comment by its ID")
    public ResponseEntity<ResponseDto<CommentDTO>> updateComment(
            @Parameter(description = "ID of the comment to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated details of the comment", required = true) @RequestBody CommentDTO commentDTO
    ) {
        commentDTO.setId(id);
        CommentDTO updatedComment = commentService.saveComment(commentDTO);
        return ResponseConfig.success(updatedComment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete a comment", description = "Deletes a comment by its ID")
    public ResponseEntity<ResponseDto<Void>> deleteComment(
            @Parameter(description = "ID of the comment to delete", required = true) @PathVariable int id
    ) {
        boolean success = commentService.deleteComment(id);
        return ResponseConfig.successDelete(null, success);
    }
}
