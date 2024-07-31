package org.example.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user in the system with unique identification and credentials.
 * <p>
 * The User entity includes an identifier, username, telephone number, email, and password.
 * Email is required to be unique, and the password is included for authentication purposes.
 * <p>
 * Note: When exporting or importing user data via Excel, passwords are excluded.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /**
     * The unique identifier for each user. It is auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    private int id;

    /**
     * The telephone number of the user. This field is mandatory.
     */
    @Column(name="telephone", nullable=false)
    private String telephone;

    /**
     * The email address of the user. This field is mandatory and must be unique.
     */
    @Column(name="email", nullable=false, unique=true)
    private String email;

    /**
     * The username of the user. This field is mandatory.
     */
    @Column(name="username", nullable=false)
    private String username;

    /**
     * The password of the user. This field is mandatory.
     */
    @Column(name="password", nullable=false)
    private String password;

    /**
     * Constructor for creating a User object without a password.
     * This is used for exporting and importing user data where passwords are not included.
     *
     * @param id        the unique identifier of the user
     * @param username  the username of the user
     * @param telephone the telephone number of the user
     * @param email     the email address of the user
     */
    public User(int id, String username, String telephone, String email) {
        this.id = id;
        this.username = username;
        this.telephone = telephone;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', telephone='%s', email='%s'}", id, username, telephone, email);
    }
}
