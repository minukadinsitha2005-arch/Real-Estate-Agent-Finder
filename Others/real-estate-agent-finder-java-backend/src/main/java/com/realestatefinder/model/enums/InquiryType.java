package com.realestatefinder.model.enums;

public enum InquiryType {
    GENERAL_INQUIRY,
    AGENT_SUPPORT,
    PROPERTY_QUESTION,
    APPOINTMENT_HELP,
    TECHNICAL_SUPPORT;

    public static InquiryType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return GENERAL_INQUIRY;
        }
        return InquiryType.valueOf(value.trim().replace(' ', '_').toUpperCase());
    }

    public String toDisplayText() {
        return name().replace('_', ' ');
    }
}
