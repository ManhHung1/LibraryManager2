package org.example.service;

import org.example.dto.BookDTO;
import org.example.interfaces.BookService;
import org.example.model.Book;
import org.example.model.User;
import org.example.repository.BookRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository; // To find the user who borrows or returns a book

    private static final int BORROW_PERIOD_DAYS = 2;        //testing

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Override
    public BookDTO uploadBookImage(int bookId, MultipartFile file) throws IOException {
        // Ensure the upload directory exists
        Files.createDirectories(fileStorageLocation);

        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path targetLocation = fileStorageLocation.resolve(fileName);

        // Copy the file to the target location
        Files.copy(file.getInputStream(), targetLocation);

        // Save the file path or URL to the book entity
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setImageUrl(targetLocation.toString()); // Or use a URL if serving files from a web server
        bookRepository.save(book);

        return convertToDTO(book);
    }

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
    public boolean deleteBook(int id) {
        bookRepository.deleteById(id);
        return true;
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
        // Calculate the end date for borrowing
        LocalDate endDate = LocalDate.now().plusDays(BORROW_PERIOD_DAYS);
        book.setBorrower(user);
        book.setBorrowedDate(LocalDate.now());
        book.setReturnedDate(endDate); // endDate is the date we have to return the book

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
        book.setBorrowedDate(null);
        book.setReturnedDate(null);

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
                book.getReturnedDate(),
                book.getImageUrl()
        );
    }

    @Override
    public List<Book> getBooksDueSoon(LocalDate fromDate, LocalDate toDate) {
        return bookRepository.findByReturnedDateBetween(fromDate, toDate);
    }


    @Override
    public boolean hasBorrowedBooks() {
        return bookRepository.countBorrowedBooks() > 0;
    }
}
