package org.example.interfaces;

import org.example.dto.CommentDTO;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getAllComments();

    CommentDTO getCommentById(int id);

    CommentDTO saveComment(CommentDTO commentDTO);

    void deleteComment(int id);
}
