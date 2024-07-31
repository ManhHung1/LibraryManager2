package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the response after authentication, containing the JWT.
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    /**
     * The JSON Web Token (JWT) issued after successful authentication.
     */
    private String jwt;
}
