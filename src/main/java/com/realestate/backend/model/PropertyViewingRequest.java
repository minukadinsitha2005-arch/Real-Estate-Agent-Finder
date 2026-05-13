package com.realestate.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "property_viewing_requests")
public class PropertyViewingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String selectedProperty;
    private String preferredViewingDate;
    private String status = "Pending";

    @Column(length = 2000)
    private String message;

    public PropertyViewingRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getSelectedProperty() { return selectedProperty; }
    public void setSelectedProperty(String selectedProperty) { this.selectedProperty = selectedProperty; }
    public String getPreferredViewingDate() { return preferredViewingDate; }
    public void setPreferredViewingDate(String preferredViewingDate) { this.preferredViewingDate = preferredViewingDate; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
