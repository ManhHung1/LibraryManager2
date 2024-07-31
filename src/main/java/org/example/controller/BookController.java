package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.BookDTO;
import org.example.interfaces.BookService;
import org.example.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Operations related to managing books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Retrieve all books", description = "Fetches a list of all books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a book by ID", description = "Fetches details of a book by its ID")
    public ResponseEntity<BookDTO> getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true) @PathVariable int id
    ) {
        BookDTO book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Adds a new book")
    public ResponseEntity<Book> createBook(
            @Parameter(description = "Details of the book to create", required = true) @RequestBody Book book
    ) {
        Book createdBook = bookService.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book", description = "Updates the details of an existing book by its ID")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID of the book to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated details of the book", required = true) @RequestBody Book book
    ) {
        book.setId(id);
        Book updatedBook = bookService.saveBook(book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of the book to delete", required = true) @PathVariable int id
    ) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/borrow")
    @Operation(summary = "Borrow a book", description = "Allows a user to borrow a book")
    public ResponseEntity<String> borrowBook(
            @Parameter(description = "ID of the user borrowing the book", required = true) @RequestParam int userId,
            @Parameter(description = "ID of the book to borrow", required = true) @RequestParam int bookId
    ) {
        String result = bookService.borrowBook(userId, bookId);
        if (result.equals("Book borrowed successfully.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping("/return")
    @Operation(summary = "Return a borrowed book", description = "Allows a user to return a borrowed book")
    public ResponseEntity<String> returnBook(
            @Parameter(description = "ID of the user returning the book", required = true) @RequestParam int userId,
            @Parameter(description = "ID of the book to return", required = true) @RequestParam int bookId
    ) {
        String result = bookService.returnBook(userId, bookId);
        if (result.equals("Book returned successfully.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
