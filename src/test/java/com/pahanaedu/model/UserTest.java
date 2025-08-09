package com.pahanaedu.model;

import com.pahanaedu.enums.UserRole;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void shouldCreateDefaultUser() {
        User user = new User();
        assertNotNull(user);
        assertEquals(0, user.getUserId());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getRole());
    }

    @Test
    void shouldCreateUserWithParameters() {
        User user = new User("test@example.com", "password123", "John", "Doe", UserRole.ADMIN);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void shouldSetAndGetUserId() {
        User user = new User();
        user.setUserId(123);
        assertEquals(123, user.getUserId());
    }

    @Test
    void shouldSetAndGetEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void shouldSetAndGetPassword() {
        User user = new User();
        user.setPassword("secret123");
        assertEquals("secret123", user.getPassword());
    }

    @Test
    void shouldSetAndGetFirstName() {
        User user = new User();
        user.setFirstName("Alice");
        assertEquals("Alice", user.getFirstName());
    }

    @Test
    void shouldSetAndGetLastName() {
        User user = new User();
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void shouldSetAndGetRole() {
        User user = new User();
        user.setRole(UserRole.CASHIER);
        assertEquals("CASHIER", user.getRole());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void shouldSetAndGetUpdatedAt() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedAt(now);
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldSetAndGetDeletedAt() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();
        user.setDeletedAt(now);
        assertEquals(now, user.getDeletedAt());
    }

    @Test
    void shouldReturnFullName() {
        User user = new User("test@example.com", "password", "John", "Doe", UserRole.ADMIN);
        assertEquals("John Doe", user.getFullName());
    }

    @Test
    void shouldCheckIfAdmin() {
        User adminUser = new User("admin@example.com", "password", "Admin", "User", UserRole.ADMIN);
        User cashierUser = new User("cashier@example.com", "password", "Cashier", "User", UserRole.CASHIER);

        assertTrue(adminUser.isAdmin());
        assertFalse(cashierUser.isAdmin());
    }

    @Test
    void shouldReturnRoleCssClass() {
        User user = new User("test@example.com", "password", "Test", "User", UserRole.ADMIN);
        assertEquals("role-admin", user.getRoleCssClass());
    }
}
