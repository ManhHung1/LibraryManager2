package org.example.interfaces;

import org.example.dto.AuthorDTO;
import org.example.model.Author;

public interface AuthorService {

    Author createAuthor(Author author);

    Author updateAuthor(int id, Author author);

    // Additional methods for getting and deleting authors can be added as needed
}
