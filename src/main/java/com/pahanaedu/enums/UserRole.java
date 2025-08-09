package com.pahanaedu.enums;

public enum UserRole {
    ADMIN,
    CASHIER;

    public String getCssClass() {
        return "role-" + name().toLowerCase();
    }

    public static UserRole fromString(String value) {
        if (value == null) {
            return null;
        }

        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Invalid user role: " + value);
    }
}
