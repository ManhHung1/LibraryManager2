package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Data Transfer Object for Author.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {

    private int id;
    private String name;
    private Set<Integer> bookIds;
}
