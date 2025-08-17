package com.pahanaedu.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {
    @Test
    void shouldHaveCorrectEnumValues() {
        UserRole[] values = UserRole.values();
        assertEquals(2, values.length);
    }

    @Test
    void shouldGenerateDisplayNames() {
        assertEquals("Administrator", UserRole.ADMIN.getDisplayName());
        assertEquals("Cashier", UserRole.CASHIER.getDisplayName());
    }

    @Test
    void shouldGenerateCssClasses() {
        assertEquals("role-admin", UserRole.ADMIN.getCssClass());
        assertEquals("role-cashier", UserRole.CASHIER.getCssClass());
    }

    @Test
    void shouldParseFromString() {
        assertEquals(UserRole.ADMIN, UserRole.fromString("ADMIN"));
        assertEquals(UserRole.CASHIER, UserRole.fromString("CASHIER"));
        assertNull(UserRole.fromString(null));
    }

    @Test
    void shouldThrowExceptionForInvalidString() {
        assertThrows(IllegalArgumentException.class, () -> UserRole.fromString("INVALID"));
    }
}
