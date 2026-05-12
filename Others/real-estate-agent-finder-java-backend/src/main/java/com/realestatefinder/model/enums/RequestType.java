package com.realestatefinder.model.enums;

public enum RequestType {
    BOOK_APPOINTMENT,
    RESCHEDULE_APPOINTMENT,
    CANCEL_APPOINTMENT,
    COMPLAINT,
    SUPPORT_REQUEST,
    GENERAL_INQUIRY;

    public static RequestType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return GENERAL_INQUIRY;
        }
        return RequestType.valueOf(value.trim().replace(' ', '_').toUpperCase());
    }

    public String toDisplayText() {
        return name().replace('_', ' ');
    }
}
