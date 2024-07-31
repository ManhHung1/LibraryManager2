package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private int id;
    private String text;
    private int bookId;
    private int userId;
}
