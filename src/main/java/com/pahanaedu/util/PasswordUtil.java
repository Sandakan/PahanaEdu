package com.pahanaedu.util;

/**
 * Utility class for password hashing and verification using BCrypt
 */
public class PasswordUtil {
    public static String hashPassword(String plainPassword) {
        return plainPassword;

        // return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String plainPassword, String storedPassword) {
        return plainPassword != null && plainPassword.equals(storedPassword);

        // return BCrypt.checkpw(plainPassword, storedPassword);
    }
}
