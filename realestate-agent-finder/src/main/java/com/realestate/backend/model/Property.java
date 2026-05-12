package com.realestate.backend.model;

import jakarta.persistence.*;

// -------------------------------------------------------
// OOP CONCEPT: ENCAPSULATION
// All fields are private. Access is through getters/setters.
// -------------------------------------------------------
@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String type;  // Apartment, House, Villa, Land, Commercial

    @Column
    private String location;

    @Column
    private double price;

    @Column
    private int bedrooms;

    @Column
    private int bathrooms;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String imageUrl;

    @Column
    private String agentName;

    @Column
    private String status = "Available"; // default status

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------
    public Property() {}

    public Property(String title, String type, String location, double price,
                    int bedrooms, int bathrooms, String description,
                    String imageUrl, String agentName, String status) {
        this.title = title;
        this.type = type;
        this.location = location;
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.description = description;
        this.imageUrl = imageUrl;
        this.agentName = agentName;
        this.status = status;
    }

    // -------------------------------------------------------
    // Getters and Setters (Encapsulation)
    // -------------------------------------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getBedrooms() { return bedrooms; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    public int getBathrooms() { return bathrooms; }
    public void setBathrooms(int bathrooms) { this.bathrooms = bathrooms; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
