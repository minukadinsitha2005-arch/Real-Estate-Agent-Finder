package com.realestatefinder.model;

import com.realestatefinder.model.enums.InquiryStatus;
import com.realestatefinder.model.enums.InquiryType;

import java.time.LocalDateTime;

public class ContactInquiry {
    private int inquiryId;
    private String fullName;
    private String email;
    private String phone;
    private String city;
    private InquiryType inquiryType;
    private String subject;
    private String message;
    private InquiryStatus status = InquiryStatus.NEW;
    private LocalDateTime createdAt;

    public int getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(int inquiryId) {
        this.inquiryId = inquiryId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? "" : fullName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? "" : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? "" : phone.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? "" : city.trim();
    }

    public InquiryType getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(InquiryType inquiryType) {
        this.inquiryType = inquiryType == null ? InquiryType.GENERAL_INQUIRY : inquiryType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? "" : subject.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? "" : message.trim();
    }

    public InquiryStatus getStatus() {
        return status;
    }

    public void setStatus(InquiryStatus status) {
        this.status = status == null ? InquiryStatus.NEW : status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
