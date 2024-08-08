package org.example.interfaces;

import org.example.model.Author;

public interface AuthorService {

    Author createAuthor(Author author);

    Author updateAuthor(int id, Author author);

    boolean deleteAuthor(int id);

}
