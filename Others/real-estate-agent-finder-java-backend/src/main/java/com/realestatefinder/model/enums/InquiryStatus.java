package com.realestatefinder.model.enums;

public enum InquiryStatus {
    NEW,
    IN_PROGRESS,
    CLOSED;

    public static InquiryStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            return NEW;
        }
        return InquiryStatus.valueOf(value.trim().replace(' ', '_').toUpperCase());
    }
}
