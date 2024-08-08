package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Data
public class BookDTO {
    private int id;
    private String title;
    private int authorId;
    private int categoryId;

    private Integer borrowerId;
    private LocalDate borrowedDate;
    private LocalDate returnedDate;
    private String imageUrl;
}
