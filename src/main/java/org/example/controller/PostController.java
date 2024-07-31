package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.PostDTO;
import org.example.interfaces.PostService;
import org.example.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a post by ID", description = "Fetches details of a post by its ID")
    public ResponseEntity<PostDTO> getPostById(
            @Parameter(description = "ID of the post to retrieve", required = true) @PathVariable int id
    ) {
        PostDTO postDTO = postService.getPostByIdDTO(id);
        if (postDTO != null) {
            return ResponseEntity.ok(postDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    @Operation(summary = "Create a new post", description = "Adds a new post")
    public ResponseEntity<Post> createPost(
            @Parameter(description = "Details of the post to create", required = true) @RequestBody Post post
    ) {
        Post createdPost = postService.savePost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing post", description = "Updates the details of an existing post by its ID")
    public ResponseEntity<Post> updatePost(
            @Parameter(description = "ID of the post to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated details of the post", required = true) @RequestBody Post post
    ) {
        post.setId(id);
        Post updatedPost = postService.savePost(post);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post", description = "Deletes a post by its ID")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "ID of the post to delete", required = true) @PathVariable int id
    ) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Retrieve posts by author", description = "Fetches posts created by a specific author")
    public ResponseEntity<List<PostDTO>> getPostsByAuthor(
            @Parameter(description = "ID of the author whose posts to retrieve", required = true) @PathVariable int authorId
    ) {
        List<PostDTO> posts = postService.getPostsByAuthor(authorId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Retrieve posts with pagination", description = "Fetches posts with pagination")
    public ResponseEntity<Page<PostDTO>> getPostsPaginated(
            @Parameter(description = "Page number to retrieve", required = true) @RequestParam int page,
            @Parameter(description = "Number of posts per page", required = true) @RequestParam int size
    ) {
        Page<PostDTO> postsPage = postService.getPostsPaginated(page, size);
        return ResponseEntity.ok(postsPage);
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
