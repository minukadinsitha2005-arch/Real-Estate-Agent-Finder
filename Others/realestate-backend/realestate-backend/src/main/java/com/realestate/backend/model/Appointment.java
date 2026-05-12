package com.realestate.backend.model;

import jakarta.persistence.*;

// -------------------------------------------------------
// OOP CONCEPT: ENCAPSULATION
// All fields are private. Access is through getters/setters.
// Fields match the appointments.html form exactly.
// -------------------------------------------------------
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column
    private String customerPhone;

    @Column
    private String city;

    @Column
    private String requestType; // Book Appointment, Reschedule, Cancel, Complaint, Support, General

    @Column
    private String agentName;

    @Column
    private String preferredDate;

    @Column
    private String preferredTime;

    @Column
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column
    private String status = "Pending"; // Pending, Approved, Rejected, Completed, Cancelled

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------
    public Appointment() {}

    // -------------------------------------------------------
    // Getters and Setters (Encapsulation)
    // -------------------------------------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getPreferredDate() { return preferredDate; }
    public void setPreferredDate(String preferredDate) { this.preferredDate = preferredDate; }

    public String getPreferredTime() { return preferredTime; }
    public void setPreferredTime(String preferredTime) { this.preferredTime = preferredTime; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
