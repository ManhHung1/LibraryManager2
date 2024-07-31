package org.example.interfaces;

import org.example.dto.BookDTO;
import org.example.model.Book;

import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();
    BookDTO getBookById(int id);
    Book saveBook(Book book);
    void deleteBook(int id);
    String borrowBook(int userId, int bookId);
    String returnBook(int userId, int bookId);
}
