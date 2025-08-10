package com.pahanaedu.service;

import com.pahanaedu.dao.UserDAO;
import com.pahanaedu.model.User;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    // Added to inject mock class in tests
    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Authenticate user with email and password
    public User authenticate(String email, String password) {
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }

        User user = userDAO.getUserByEmail(email.trim().toLowerCase());

        if (user != null && password.equals(user.getPassword())) {
            userDAO.updateLastLogin(user.getUserId());
            return user;
        }

        return null;
    }
}
