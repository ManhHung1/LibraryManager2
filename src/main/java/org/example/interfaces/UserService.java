package org.example.interfaces;

import org.example.dto.UserDTO;
import org.example.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsersDTO();

    List<User> getAllUsers();

    User createUser(User newUser);

    boolean deleteUser(Integer id);

    User updateUser(Integer id, User updatedUser);

    List<UserDTO> searchUsersByUsername(String username);

    List<UserDTO> searchUsersByEmail(String email);

    Page<UserDTO> getAllUsersPaginated(int page, int size);

    User getUserById(Integer id);

    UserDTO getUserByUsername(String username);
}
