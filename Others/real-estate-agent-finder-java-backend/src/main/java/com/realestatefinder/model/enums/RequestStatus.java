package com.realestatefinder.model.enums;

public enum RequestStatus {
    PENDING,
    CONFIRMED,
    RESCHEDULED,
    CANCELLED,
    RESOLVED;

    public static RequestStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            return PENDING;
        }
        return RequestStatus.valueOf(value.trim().replace(' ', '_').toUpperCase());
    }
}
