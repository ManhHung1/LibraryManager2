package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for User.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private int id;
    private String username;
    private String telephone;
    private String email;

    public UserDTO(String username, String telephone, String email) {
        this.username = username;
        this.telephone = telephone;
        this.email = email;
    }
}
