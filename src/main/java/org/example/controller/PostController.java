package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.config.ResponseConfig;
import org.example.dto.PostDTO;
import org.example.dto.ResponseDto;
import org.example.interfaces.PostService;
import org.example.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Operations related to managing posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "Retrieve all posts", description = "Fetches a list of all posts")
    public ResponseEntity<ResponseDto<List<PostDTO>>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseConfig.success(posts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a post by ID", description = "Fetches details of a post by its ID")
    public ResponseEntity<ResponseDto<PostDTO>> getPostById(
            @Parameter(description = "ID of the post to retrieve", required = true) @PathVariable int id
    ) {
        PostDTO postDTO = postService.getPostByIdDTO(id);
        if (postDTO != null) {
            return ResponseConfig.success(postDTO);
        } else {
            return ResponseConfig.error(HttpStatus.NOT_FOUND, "01", "Post not found");
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a new post", description = "Adds a new post")
    public ResponseEntity<ResponseDto<PostDTO>> createPost(
            @Parameter(description = "Details of the post to create", required = true) @RequestBody Post post
    ) {
        Post createdPost = postService.savePost(post);
        PostDTO postDTO = convertToDTO(createdPost);
        return ResponseConfig.success(postDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update an existing post", description = "Updates the details of an existing post by its ID")
    public ResponseEntity<ResponseDto<PostDTO>> updatePost(
            @Parameter(description = "ID of the post to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated details of the post", required = true) @RequestBody Post post
    ) {
        post.setId(id);
        Post updatedPost = postService.savePost(post);
        PostDTO postDTO = convertToDTO(updatedPost);
        return ResponseConfig.success(postDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete a post", description = "Deletes a post by its ID")
    public ResponseEntity<ResponseDto<Void>> deletePost(
            @Parameter(description = "ID of the post to delete", required = true) @PathVariable int id
    ) {
        boolean success = postService.deletePost(id);
        return ResponseConfig.successDelete(null, success);
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Retrieve posts by author", description = "Fetches posts created by a specific author")
    public ResponseEntity<ResponseDto<List<PostDTO>>> getPostsByAuthor(
            @Parameter(description = "ID of the author whose posts to retrieve", required = true) @PathVariable int authorId
    ) {
        List<PostDTO> posts = postService.getPostsByAuthor(authorId);
        return ResponseConfig.success(posts);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Retrieve posts with pagination", description = "Fetches posts with pagination")
    public ResponseEntity<ResponseDto<Page<PostDTO>>> getPostsPaginated(
            @Parameter(description = "Page number to retrieve", required = true) @RequestParam int page,
            @Parameter(description = "Number of posts per page", required = true) @RequestParam int size
    ) {
        Page<PostDTO> postsPage = postService.getPostsPaginated(page, size);
        return ResponseConfig.success(postsPage);
    }

    private PostDTO convertToDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor() != null ? post.getAuthor().getId() : 0
        );
    }
}
