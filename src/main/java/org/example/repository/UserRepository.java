package org.example.repository;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * It extends JpaRepository to provide CRUD operations and additional query methods such as findAll(), findById(int) or deleteById(int).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByEmailContainingIgnoreCase(String email);
    User findByUsername(String username);
    Optional<User> findOptByUsername(String username);
}
