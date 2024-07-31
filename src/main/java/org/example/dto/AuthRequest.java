package org.example.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the authentication request containing username and password.
 */
@Getter
@Setter
public class AuthRequest {

    /**
     * The username for authentication.
     */
    private String username;

    /**
     * The password for authentication.
     */
    private String password;

}
