package com.pahanaedu.dao;

import com.pahanaedu.helpers.DatabaseHelper;
import com.pahanaedu.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;

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
                            "(1, 'admin@pahanaedu.com', 'admin123', 'Admin', 'User', 'ADMIN', NOW(), NOW()), " +
                            "(2, 'cashier@pahanaedu.com', 'cashier123', 'Cashier', 'User', 'CASHIER', NOW(), NOW())");

        }
    }

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    void shouldRetrieveExistingUsers() {

        List<User> users = userDAO.getAllUsers();

        assertNotNull(users, "Users list should not be null");
        assertFalse(users.isEmpty(), "Should have users from seed data");

        assertTrue(users.size() >= 2, "Should have at least 2 seed users");

        boolean hasAdmin = users.stream().anyMatch(u -> "admin@pahanaedu.com".equals(u.getEmail()));
        boolean hasCashier = users.stream().anyMatch(u -> "cashier@pahanaedu.com".equals(u.getEmail()));

        assertTrue(hasAdmin, "Should have admin user from seed data");
        assertTrue(hasCashier, "Should have cashier user from seed data");
    }

    @Test
    void shouldRetrieveUserById() {

        User user = userDAO.getUserById(1);

        assertNotNull(user, "Should find user with ID 1");
        assertEquals("admin@pahanaedu.com", user.getEmail(), "User ID 1 should be admin");
        assertEquals("Admin", user.getFirstName(), "First name should match seed data");
        assertEquals("User", user.getLastName(), "Last name should match seed data");
        assertEquals("ADMIN", user.getRole(), "Role should match seed data");
    }

    @Test
    void shouldReturnNullForNonExistentUser() {

        User result = userDAO.getUserById(99999);
        assertNull(result, "Non-existent user should return null");
    }

    @Test
    void shouldHandleInvalidIds() {

        assertNull(userDAO.getUserById(-1), "Negative ID should return null");
        assertNull(userDAO.getUserById(0), "Zero ID should return null");
    }

    @Test
    void shouldRetrieveUserByEmail() {

        User user = userDAO.getUserByEmail("admin@pahanaedu.com");

        assertNotNull(user, "Should find admin user by email");
        assertEquals("admin@pahanaedu.com", user.getEmail(), "Email should match");
        assertEquals("Admin", user.getFirstName(), "First name should match");
        assertEquals("ADMIN", user.getRole(), "Role should match");
    }

    @Test
    void shouldReturnNullForNonExistentEmail() {

        User result = userDAO.getUserByEmail("nonexistent@example.com");
        assertNull(result, "Non-existent email should return null");
    }

    @Test
    void shouldCreateNewUser() {

        User newUser = new User();
        newUser.setEmail("test" + System.currentTimeMillis() + "@pahanaedu.com");
        newUser.setPassword("testpass123");
        newUser.setFirstName("Test");
        newUser.setLastName("User");
        newUser.setRole("CASHIER");

        User createdUser = userDAO.createUser(newUser);
        assertNotNull(createdUser, "Should successfully create new user");
        assertTrue(createdUser.getUserId() > 0, "Created user should have positive ID");

        User retrievedUser = userDAO.getUserById(createdUser.getUserId());
        assertNotNull(retrievedUser, "Should be able to retrieve created user");
        assertEquals(newUser.getEmail(), retrievedUser.getEmail(), "Email should match");
        assertEquals(newUser.getFirstName(), retrievedUser.getFirstName(), "First name should match");
    }

    @Test
    void shouldUpdateExistingUser() {

        User testUser = new User();
        testUser.setEmail("update" + System.currentTimeMillis() + "@pahanaedu.com");
        testUser.setPassword("originalpass");
        testUser.setFirstName("Original");
        testUser.setLastName("Name");
        testUser.setRole("CASHIER");

        User createdUser = userDAO.createUser(testUser);
        assertNotNull(createdUser, "Should create test user");

        createdUser.setFirstName("Updated");
        createdUser.setLastName("User");
        createdUser.setRole("ADMIN");

        boolean updated = userDAO.updateUser(createdUser, "newpassword");
        assertTrue(updated, "Should successfully update user");

        User updatedUser = userDAO.getUserById(createdUser.getUserId());
        assertNotNull(updatedUser, "Should find the updated user");
        assertEquals("Updated", updatedUser.getFirstName(), "First name should be updated");
        assertEquals("User", updatedUser.getLastName(), "Last name should be updated");
        assertEquals("ADMIN", updatedUser.getRole(), "Role should be updated");
    }

    @Test
    void shouldUpdateUserWithoutPassword() {

        User testUser = new User();
        testUser.setEmail("nopassupdate" + System.currentTimeMillis() + "@pahanaedu.com");
        testUser.setPassword("keepthispass");
        testUser.setFirstName("Keep");
        testUser.setLastName("Password");
        testUser.setRole("CASHIER");

        User createdUser = userDAO.createUser(testUser);
        assertNotNull(createdUser, "Should create test user");

        createdUser.setFirstName("Changed");
        boolean updated = userDAO.updateUser(createdUser, null);
        assertTrue(updated, "Should successfully update user without password change");

        User updatedUser = userDAO.getUserById(createdUser.getUserId());
        assertEquals("Changed", updatedUser.getFirstName(), "First name should be updated");
    }

    @Test
    void shouldDeleteUser() {

        User userToDelete = new User();
        userToDelete.setEmail("delete" + System.currentTimeMillis() + "@pahanaedu.com");
        userToDelete.setPassword("deletepass");
        userToDelete.setFirstName("Delete");
        userToDelete.setLastName("Me");
        userToDelete.setRole("CASHIER");

        User createdUser = userDAO.createUser(userToDelete);
        assertNotNull(createdUser, "Should create temporary user");

        boolean deleted = userDAO.deleteUser(createdUser.getUserId());
        assertTrue(deleted, "Should successfully delete user");

        User deletedUser = userDAO.getUserById(createdUser.getUserId());
        assertNull(deletedUser, "Deleted user should not be retrievable");
    }

    @Test
    void shouldCheckEmailExists() {

        assertTrue(userDAO.emailExists("admin@pahanaedu.com"), "Should find existing admin email");
        assertFalse(userDAO.emailExists("nonexistent@example.com"), "Should not find non-existent email");
    }

    @Test
    void shouldCheckEmailExistsWithExclusion() {

        assertFalse(userDAO.emailExists("admin@pahanaedu.com", 1), "Should exclude user ID 1 (admin)");
        assertTrue(userDAO.emailExists("admin@pahanaedu.com", 2),
                "Should find admin email when excluding different user");
    }

    @Test
    void shouldUpdateLastLogin() {

        boolean updated = userDAO.updateLastLogin(1);
        assertTrue(updated, "Should successfully update last login for existing user");

        assertFalse(userDAO.updateLastLogin(99999), "Should return false for non-existent user");
    }

    @Test
    void shouldHandleNullAndInvalidInputs() {

        assertNull(userDAO.createUser(null), "Creating null user should return null");
        assertFalse(userDAO.updateUser(null, "password"), "Updating null user should return false");
        assertFalse(userDAO.deleteUser(-1), "Deleting with negative ID should return false");
        assertFalse(userDAO.emailExists(null), "Checking null email should return false");
        assertFalse(userDAO.emailExists(""), "Checking empty email should return false");
        assertNull(userDAO.getUserByEmail(null), "Getting user with null email should return null");
        assertNull(userDAO.getUserByEmail(""), "Getting user with empty email should return null");
    }

    @Test
    void shouldValidateUserCreationRequirements() {

        User invalidUser = new User();

        invalidUser.setPassword("password");
        invalidUser.setFirstName("Test");
        invalidUser.setLastName("User");
        invalidUser.setRole("CASHIER");

        assertNull(userDAO.createUser(invalidUser), "User without email should fail to create");

        User duplicateEmailUser = new User();
        duplicateEmailUser.setEmail("admin@pahanaedu.com");
        duplicateEmailUser.setPassword("password");
        duplicateEmailUser.setFirstName("Duplicate");
        duplicateEmailUser.setLastName("User");
        duplicateEmailUser.setRole("CASHIER");

        assertNull(userDAO.createUser(duplicateEmailUser), "User with duplicate email should fail to create");
    }
}
