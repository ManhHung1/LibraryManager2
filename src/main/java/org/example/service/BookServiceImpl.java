package org.example.service;

import org.example.dto.BookDTO;
import org.example.interfaces.BookService;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Category;
import org.example.model.User;
import org.example.repository.BookRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository; // To find the user who borrows or returns a book

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(int id) {
        return bookRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public String borrowBook(int userId, int bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (bookOpt.isEmpty()) {
            return "Book not found.";
        }
        if (userOpt.isEmpty()) {
            return "User not found.";
        }

        Book book = bookOpt.get();
        User user = userOpt.get();

        // Check if the book is already borrowed
        if (book.getBorrower() != null) {
            return "Book is already borrowed.";
        }

        // Borrow the book
        book.setBorrower(user);
        book.setBorrowedDate(LocalDate.now());
        book.setReturnedDate(null); // Reset the returned date as it is now borrowed

        bookRepository.save(book);

        return "Book borrowed successfully.";
    }

    @Override
    @Transactional
    public String returnBook(int userId, int bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (bookOpt.isEmpty()) {
            return "Book not found.";
        }
        if (userOpt.isEmpty()) {
            return "User not found.";
        }

        Book book = bookOpt.get();
        User user = userOpt.get();

        // Check if the book is borrowed by the user
        if (book.getBorrower() == null || !book.getBorrower().equals(user)) {
            return "You cannot return a book you did not borrow.";
        }

        // Return the book
        book.setBorrower(null);
        book.setReturnedDate(LocalDate.now());

        bookRepository.save(book);

        return "Book returned successfully.";
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor() != null ? book.getAuthor().getId() : null,
                book.getCategory() != null ? book.getCategory().getId() : null,
                book.getBorrower() != null ? book.getBorrower().getId() : null,
                book.getBorrowedDate(),
                book.getReturnedDate()
        );
    }

}
