package org.example.interfaces;

import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.model.Author;
import org.example.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AuthenticationService {
    AuthResponse authenticate(AuthRequest authRequest) throws AuthenticationException;

    @Service
    class AuthorService {

        @Autowired
        private AuthorRepository authorRepository;

        public List<Author> getAllAuthors() {
            return authorRepository.findAll();
        }

        public Author getAuthorById(int id) {
            return authorRepository.findById(id).orElse(null);
        }

        public Author saveAuthor(Author author) {
            return authorRepository.save(author);
        }

        public void deleteAuthor(int id) {
            authorRepository.deleteById(id);
        }
    }
}