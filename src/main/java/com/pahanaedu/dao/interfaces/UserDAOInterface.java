package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.User;

import java.util.List;

public interface UserDAOInterface {
    List<User> getAllUsers();

    User createUser(User user);

    User getUserById(int userId);

    boolean updateUser(User user, String newPassword);

    boolean deleteUser(int userId);

    boolean emailExists(String email, int excludeUserId);

    boolean emailExists(String email);

    User getUserByEmail(String email);

    boolean updateLastLogin(int userId);
}
