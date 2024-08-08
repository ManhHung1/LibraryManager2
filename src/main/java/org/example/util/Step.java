package org.example.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class Step {

    private int id;

    private String name;

    private String code;

    public Set<Information> information;

}
