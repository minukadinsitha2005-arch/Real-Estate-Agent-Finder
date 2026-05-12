package com.realestatefinder.model.enums;

public enum PropertyType {
    APARTMENT,
    HOUSE,
    VILLA,
    LAND,
    COMMERCIAL;

    public static PropertyType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return HOUSE;
        }
        return PropertyType.valueOf(value.trim().toUpperCase());
    }
}
