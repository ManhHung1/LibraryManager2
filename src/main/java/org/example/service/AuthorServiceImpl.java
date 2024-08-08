package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.interfaces.AuthorService;
import org.example.model.Author;
import org.example.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;

    @Override
    public Author createAuthor(Author author) {
        logger.info("Creating author with name: {}", author.getName());
        Author savedAuthor = authorRepository.save(author);
        logger.debug("Author with name: {} created successfully.", savedAuthor.getName());
        return savedAuthor;
    }

    @Override
    public Author updateAuthor(int id, Author author) {
        logger.info("Updating author with ID: {}", id);
        Optional<Author> existingAuthorOpt = authorRepository.findById(id);

        if (existingAuthorOpt.isPresent()) {
            Author existingAuthor = existingAuthorOpt.get();
            existingAuthor.setName(author.getName());
            existingAuthor.setBooks(author.getBooks());

            Author updatedAuthor = authorRepository.save(existingAuthor);
            logger.debug("Author with ID: {} updated successfully.", id);
            return updatedAuthor;
        } else {
            logger.warn("Author with ID: {} not found.", id);
            return null; // or throw an exception if preferred
        }
    }

    @Override
    public boolean deleteAuthor(int id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
