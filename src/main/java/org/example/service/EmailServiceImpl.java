package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.interfaces.EmailService;
import org.example.model.Book;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReminderEmail(User user, Book book) {
        String to = user.getEmail();
        String subject = "Book Return Reminder";
        String text = "Dear " + user.getUsername() + ",\n\n" +
                "This is a reminder that the book titled '" + book.getTitle() +
                "' is due to be returned on " + book.getReturnedDate() + ".\n" +
                "Please ensure that you return the book on or before the due date.\n\n" +
                "Thank you,\nLibrary Management";

        sendEmail(to, subject, text);
    }

    public void sendEmail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
