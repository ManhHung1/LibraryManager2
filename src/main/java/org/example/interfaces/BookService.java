package org.example.interfaces;

import org.example.dto.BookDTO;
import org.example.model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();
    BookDTO getBookById(int id);
    Book saveBook(Book book);
    boolean deleteBook(int id);
    String borrowBook(int userId, int bookId);
    String returnBook(int userId, int bookId);
    BookDTO uploadBookImage(int bookId, MultipartFile file) throws IOException;
    public List<Book> getBooksDueSoon(LocalDate fromDate, LocalDate toDate);
    boolean hasBorrowedBooks();
}
