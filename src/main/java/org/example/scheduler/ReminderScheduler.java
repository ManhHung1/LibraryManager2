package org.example.scheduler;

import org.example.config.AppConfig;
import org.example.interfaces.BookService;
import org.example.interfaces.EmailService;
import org.example.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

// It sends email every minute (test) if there is any books borrowed by anybody
@Component
public class ReminderScheduler {

    @Autowired
    private BookService bookService;

    @Autowired
    private EmailService emailService; // Assuming you have a service to send emails

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AppConfig.class);

    @Scheduled(cron = "0 * * * * ?") // Runs every minute
    public void sendReminderEmails() {
        logger.info("YES");
        logger.info("{}",bookService.hasBorrowedBooks());
        if (bookService.hasBorrowedBooks()) {
            LocalDate now = LocalDate.now();
            LocalDate reminderDate = now.plusDays(2);
            List<Book> booksDueSoon = bookService.getBooksDueSoon(now, reminderDate);

            for (Book book : booksDueSoon) {
                // Send reminder email to the borrower
                emailService.sendReminderEmail(book.getBorrower(), book);
                logger.info("Sent reminder email for book: {} to user: {}", book.getTitle(), book.getBorrower().getUsername());
            }
        }
    }
}
