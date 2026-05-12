package com.realestatefinder.model.enums;

public enum UserRole {
    ADMIN,
    CUSTOMER;

    public static UserRole fromValue(String value) {
        if (value == null || value.isBlank()) {
            return CUSTOMER;
        }
        return UserRole.valueOf(value.trim().toUpperCase());
    }
}
