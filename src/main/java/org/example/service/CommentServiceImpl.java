package org.example.service;

import org.example.dto.CommentDTO;
import org.example.model.Comment;
import org.example.model.Book;
import org.example.model.User;
import org.example.repository.CommentRepository;
import org.example.repository.BookRepository;
import org.example.repository.UserRepository;
import org.example.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(int id) {
        return commentRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public CommentDTO saveComment(CommentDTO commentDTO) {
        Comment comment = convertToEntity(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    @Override
    public boolean deleteComment(int id) {
        commentRepository.deleteById(id);
        return false;
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setBookId(comment.getBook().getId());
        dto.setUserId(comment.getAuthor().getId());
        return dto;
    }

    private Comment convertToEntity(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setText(dto.getText());
        Book book = bookRepository.findById(dto.getBookId()).orElse(null);
        User user = userRepository.findById(dto.getUserId()).orElse(null);
        comment.setBook(book);
        comment.setAuthor(user);
        return comment;
    }
}
