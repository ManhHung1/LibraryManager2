package org.example.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * A utility class for generating a 256-bit secret key used for signing JWT tokens.
 * This key is necessary for creating and validating JWTs using the HS256 algorithm.
 * The generated key should be securely stored and used for JWT operations in your application.
 */
public class KeyGenerator {

    /**
     * The main method that generates a new 256-bit secret key for JWT tokens.
     * The key is printed to the console in Base64 encoded format.
     * This will output the generated key which should be copied and stored securely.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        var key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println("Generated Key: " + java.util.Base64.getEncoder().encodeToString(key.getEncoded()));
    }
}
