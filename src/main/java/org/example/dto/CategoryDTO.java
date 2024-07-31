package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class CategoryDTO {

    private int id;
    private String name;
    private Set<Integer> bookIds;

}
