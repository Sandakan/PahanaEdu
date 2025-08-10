package com.pahanaedu.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void shouldHashPasswordAsPlainText() {
        String password = "testPassword123";
        String hashedPassword = PasswordUtil.hashPassword(password);
        assertNotNull(hashedPassword);
        assertEquals(password, hashedPassword);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldHandleInvalidPasswordsForHashing(String password) {
        String hashedPassword = PasswordUtil.hashPassword(password);
        assertEquals(password, hashedPassword);
    }

    @Test
    void shouldVerifyMatchingPasswords() {
        String password = "myPassword123";
        assertTrue(PasswordUtil.verifyPassword(password, password));
    }

    @Test
    void shouldRejectDifferentPasswords() {
        assertFalse(PasswordUtil.verifyPassword("wrongPassword", "correctPassword"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "password with spaces", "p@ssw0rd!#$%", "123456789" })
    void shouldVerifyVariousPasswordFormats(String password) {
        assertTrue(PasswordUtil.verifyPassword(password, password));
    }

    @Test
    void shouldHandleNullInputsInVerification() {
        assertFalse(PasswordUtil.verifyPassword(null, "password"));
        assertFalse(PasswordUtil.verifyPassword("password", null));
        assertFalse(PasswordUtil.verifyPassword(null, null));
    }

    @Test
    void shouldHandleEmptyInputsInVerification() {
        assertTrue(PasswordUtil.verifyPassword("", ""));
        assertFalse(PasswordUtil.verifyPassword("", "password"));
        assertFalse(PasswordUtil.verifyPassword("password", ""));
    }
}
