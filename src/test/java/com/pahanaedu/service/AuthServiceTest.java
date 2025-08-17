package com.pahanaedu.service;

import com.pahanaedu.helpers.DatabaseHelper;
import com.pahanaedu.model.User;
import com.pahanaedu.enums.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeAll
    static void setupDatabase() throws SQLException {

        try (Connection connection = DatabaseHelper.getInstance().getConnection();
                Statement statement = connection.createStatement()) {

            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            statement.execute("DELETE FROM users");
            statement.execute("ALTER TABLE users AUTO_INCREMENT = 1");

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");

            statement.execute(
                    "INSERT INTO users (user_id, email, password, first_name, last_name, role, created_at, updated_at) VALUES "
                            +
                            "(1, 'admin@pahanaedu.com', 'admin123', 'Admin', 'User', 'ADMIN', NOW(), NOW())");

            statement.execute(
                    "INSERT INTO users (user_id, email, password, first_name, last_name, role, created_at, updated_at) VALUES "
                            +
                            "(2, 'cashier@pahanaedu.com', 'cashier123', 'Cashier', 'User', 'CASHIER', NOW(), NOW())");

            statement.execute(
                    "INSERT INTO users (user_id, email, password, first_name, last_name, role, created_at, updated_at) VALUES "
                            +
                            "(3, 'test@example.com', 'password123', 'Test', 'User', 'CASHIER', NOW(), NOW())");

        }
    }

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void shouldCreateAuthServiceWithDefaultConstructor() {
        AuthService service = new AuthService();
        assertNotNull(service);
    }

    @Test
    void shouldReturnNullForInvalidInputs() {
        assertNull(authService.authenticate(null, "password"));
        assertNull(authService.authenticate("admin@pahanaedu.com", null));
        assertNull(authService.authenticate("", "password"));
        assertNull(authService.authenticate("admin@pahanaedu.com", ""));
        assertNull(authService.authenticate("   ", "password"));
        assertNull(authService.authenticate("admin@pahanaedu.com", "   "));
        assertNull(authService.authenticate(null, null));
        assertNull(authService.authenticate("", ""));
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        String email = "nonexistent@test.com";
        String password = "password123";

        User result = authService.authenticate(email, password);

        assertNull(result, "Should return null for non-existent user");
    }

    @Test
    void shouldReturnNullWhenPasswordIncorrect() {
        String email = "admin@pahanaedu.com";
        String password = "wrongPassword";

        User result = authService.authenticate(email, password);

        assertNull(result, "Should return null for incorrect password");
    }

    @Test
    void shouldReturnUserWhenCredentialsValid() {
        String email = "admin@pahanaedu.com";
        String password = "admin123";

        User result = authService.authenticate(email, password);

        assertNotNull(result, "Should return user for valid credentials");
        assertEquals(email, result.getEmail());
        assertEquals("Admin", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals(UserRole.ADMIN, result.getRoleEnum());
        assertEquals(1, result.getUserId());
    }

    @Test
    void shouldReturnCashierUserWhenCredentialsValid() {
        String email = "cashier@pahanaedu.com";
        String password = "cashier123";

        User result = authService.authenticate(email, password);

        assertNotNull(result, "Should return cashier user for valid credentials");
        assertEquals(email, result.getEmail());
        assertEquals("Cashier", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals(UserRole.CASHIER, result.getRoleEnum());
        assertEquals(2, result.getUserId());
    }

    @Test
    void shouldTrimAndLowercaseEmail() {
        String email = "  ADMIN@PAHANAEDU.COM  ";
        String expectedEmail = "admin@pahanaedu.com";
        String password = "admin123";

        User result = authService.authenticate(email, password);

        assertNotNull(result, "Should return user for trimmed and lowercased email");
        assertEquals(expectedEmail, result.getEmail());
        assertEquals("Admin", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals(UserRole.ADMIN, result.getRoleEnum());
        assertEquals(1, result.getUserId());
    }

    @Test
    void shouldReturnTestUserWhenCredentialsValid() {
        String email = "test@example.com";
        String password = "password123";

        User result = authService.authenticate(email, password);

        assertNotNull(result, "Should return test user for valid credentials");
        assertEquals(email, result.getEmail());
        assertEquals("Test", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals(UserRole.CASHIER, result.getRoleEnum());
        assertEquals(3, result.getUserId());
    }
}
