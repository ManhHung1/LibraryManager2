package org.example.service;

import org.example.interfaces.StatisticsService;
import org.example.repository.BookRepository;
import org.example.repository.CategoryRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public int getTotalBooks() {
        return (int) bookRepository.count();
    }

    public int getTotalCategories() {
        return (int) categoryRepository.count();
    }

    public int getTotalUsers() {
        return (int) userRepository.count();
    }

}
