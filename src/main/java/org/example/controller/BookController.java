package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.config.ResponseConfig;
import org.example.dto.BookDTO;
import org.example.dto.ResponseDto;
import org.example.interfaces.BookService;
import org.example.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Operations related to managing books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Retrieve all books", description = "Fetches a list of all books")
    public ResponseEntity<ResponseDto<List<BookDTO>>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseConfig.success(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a book by ID", description = "Fetches details of a book by its ID")
    public ResponseEntity<ResponseDto<BookDTO>> getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true) @PathVariable int id
    ) {
        BookDTO book = bookService.getBookById(id);
        if (book != null) {
            return ResponseConfig.success(book);
        } else {
            return ResponseConfig.error(HttpStatus.NOT_FOUND, "01", "Book not found");
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a new book", description = "Adds a new book")
    public ResponseEntity<ResponseDto<Book>> createBook(
            @Parameter(description = "Details of the book to create", required = true) @RequestBody Book book
    ) {
        Book createdBook = bookService.saveBook(book);
        return ResponseConfig.success(createdBook);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update an existing book", description = "Updates the details of an existing book by its ID")
    public ResponseEntity<ResponseDto<Book>> updateBook(
            @Parameter(description = "ID of the book to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated details of the book", required = true) @RequestBody Book book
    ) {
        book.setId(id);
        Book updatedBook = bookService.saveBook(book);
        return ResponseConfig.success(updatedBook);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    public ResponseEntity<ResponseDto<Void>> deleteBook(
            @Parameter(description = "ID of the book to delete", required = true) @PathVariable int id
    ) {
        boolean success = bookService.deleteBook(id);
        return ResponseConfig.successDelete(null, success);
    }

    @PostMapping("/uploadImage/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Upload an image for a book", description = "Uploads an image and associates it with a book")
    public ResponseEntity<ResponseDto<BookDTO>> uploadBookImage(
            @Parameter(description = "ID of the book to associate the image with", required = true) @PathVariable int id,
            @Parameter(description = "The image file to upload", required = true) @RequestParam("file") MultipartFile file
    ) throws IOException {
        BookDTO updatedBook = bookService.uploadBookImage(id, file);
        return ResponseConfig.success(updatedBook);
    }

    @PostMapping("/borrow")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @Operation(summary = "Borrow a book", description = "Allows a user to borrow a book")
    public ResponseEntity<ResponseDto<String>> borrowBook(
            @Parameter(description = "ID of the book to borrow", required = true) @RequestParam int bookId,
            @Parameter(description = "ID of the user borrowing the book", required = true) @RequestParam int userId
    ) {
        String result = bookService.borrowBook(userId, bookId);
        if ("Book borrowed successfully.".equals(result)) {
            return ResponseConfig.success(result);
        } else {
            return ResponseConfig.error(HttpStatus.BAD_REQUEST, result, "01");
        }
    }

    @PostMapping("/return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @Operation(summary = "Return a borrowed book", description = "Allows a user to return a borrowed book")
    public ResponseEntity<ResponseDto<String>> returnBook(
            @Parameter(description = "ID of the user returning the book", required = true) @RequestParam int userId,
            @Parameter(description = "ID of the book to return", required = true) @RequestParam int bookId
    ) {
        String result = bookService.returnBook(userId, bookId);
        if ("Book returned successfully.".equals(result)) {
            return ResponseConfig.success(result);
        } else {
            return ResponseConfig.error(HttpStatus.BAD_REQUEST, result, "01");
        }
    }
}
