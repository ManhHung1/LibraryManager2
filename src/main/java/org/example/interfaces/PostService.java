package org.example.interfaces;

import org.example.dto.PostDTO;
import org.example.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(int id);
    Post savePost(Post post);
    void deletePost(int id);

    PostDTO getPostByIdDTO(int id);

    List<PostDTO> getPostsByAuthor(int authorId);

    Page<PostDTO> getPostsPaginated(int page, int size);
}
