package org.example.interfaces;

import org.example.model.Book;
import org.example.model.User;

public interface EmailService {
    public void sendReminderEmail(User user, Book book);
    public void sendEmail(String to, String subject, String text);
}
