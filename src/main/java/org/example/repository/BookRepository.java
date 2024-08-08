package org.example.repository;

import org.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    long countByBorrowerId(int userId);
    List<Book> findByReturnedDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.borrowedDate IS NOT NULL AND b.returnedDate IS NOT NULL")
    int countBorrowedBooks();
}
