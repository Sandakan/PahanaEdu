package com.pahanaedu.service;

import com.pahanaedu.dao.UserDAO;
import com.pahanaedu.model.User;

/**
 * Service class for user authentication and authorization
 */
public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
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

        // if (user != null && PasswordUtil.verifyPassword(password,
        // user.getPassword())) {
        // userDAO.updateLastLogin(user.getUserId());
        // return user;
        // }

        return null;
    }

    /**
     * Check if user has required role
     *
     * @param user         the user object
     * @param requiredRole the required role
     * @return true if user has the required role, false otherwise
     */
    public boolean hasRole(User user, String requiredRole) {
        return user != null && requiredRole.equals(user.getRole());
    }
}
