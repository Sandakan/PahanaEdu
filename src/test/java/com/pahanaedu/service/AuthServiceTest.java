package com.pahanaedu.service;

import com.pahanaedu.dao.UserDAO;
import com.pahanaedu.model.User;
import com.pahanaedu.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserDAO userDAO;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userDAO);
    }

    @Test
    void shouldCreateAuthServiceWithDefaultConstructor() {
        AuthService service = new AuthService();
        assertNotNull(service);
    }

    @Test
    void shouldCreateAuthServiceWithUserDAO() {
        AuthService service = new AuthService(userDAO);
        assertNotNull(service);
    }

    @Test
    void shouldReturnNullForInvalidInputs() {
        assertNull(authService.authenticate(null, "password"));
        assertNull(authService.authenticate("email@test.com", null));
        assertNull(authService.authenticate("", "password"));
        assertNull(authService.authenticate("email@test.com", ""));
        assertNull(authService.authenticate("   ", "password"));
        assertNull(authService.authenticate("email@test.com", "   "));
        assertNull(authService.authenticate(null, null));
        assertNull(authService.authenticate("", ""));

        verifyNoInteractions(userDAO);
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        String email = "nonexistent@test.com";
        String password = "password123";

        when(userDAO.getUserByEmail(email)).thenReturn(null);

        User result = authService.authenticate(email, password);

        assertNull(result);
        verify(userDAO).getUserByEmail(email);
        verify(userDAO, never()).updateLastLogin(anyInt());
    }

    @Test
    void shouldReturnNullWhenPasswordIncorrect() {
        String email = "test@example.com";
        String password = "wrongPassword";
        User mockUser = new User(email, "correctPassword", "John", "Doe", UserRole.ADMIN);
        mockUser.setUserId(1);

        when(userDAO.getUserByEmail(email)).thenReturn(mockUser);

        User result = authService.authenticate(email, password);

        assertNull(result);
        verify(userDAO).getUserByEmail(email);
        verify(userDAO, never()).updateLastLogin(anyInt());
    }

    @Test
    void shouldReturnUserWhenCredentialsValid() {
        String email = "test@example.com";
        String password = "correctPassword";
        User mockUser = new User(email, password, "John", "Doe", UserRole.ADMIN);
        mockUser.setUserId(1);

        when(userDAO.getUserByEmail(email)).thenReturn(mockUser);

        User result = authService.authenticate(email, password);

        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(userDAO).getUserByEmail(email);
        verify(userDAO).updateLastLogin(1);
    }

    @Test
    void shouldTrimAndLowercaseEmail() {
        String email = "  TEST@EXAMPLE.COM  ";
        String expectedEmail = "test@example.com";
        String password = "password123";
        User mockUser = new User(expectedEmail, password, "John", "Doe", UserRole.ADMIN);
        mockUser.setUserId(1);

        when(userDAO.getUserByEmail(expectedEmail)).thenReturn(mockUser);

        User result = authService.authenticate(email, password);

        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(userDAO).getUserByEmail(expectedEmail);
        verify(userDAO).updateLastLogin(1);
    }
}
