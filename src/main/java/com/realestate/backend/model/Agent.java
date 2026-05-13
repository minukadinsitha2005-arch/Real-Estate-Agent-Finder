package com.realestate.backend.model;

import jakarta.persistence.*;

// -------------------------------------------------------
// OOP CONCEPT: INHERITANCE
// Agent extends Person, so it inherits id, name, email,
// and phone from the Person class automatically.
// -------------------------------------------------------
@Entity
@Table(name = "agents")
public class Agent extends Person {

    // OOP CONCEPT: ENCAPSULATION - all fields are private
    @Column
    private String location;

    @Column
    private String specialization;

    @Column
    private int experienceYears;

    @Column
    private String imageUrl;

    @Column
    private double rating;

    @Column
    private String badge;

    @Column(columnDefinition = "TEXT")
    private String description;

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------
    public Agent() {}

    public Agent(String name, String email, String phone,
                 String location, String specialization, int experienceYears,
                 String imageUrl, double rating, String badge, String description) {
        super(name, email, phone); // calls Person constructor
        this.location = location;
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.badge = badge;
        this.description = description;
    }

    // -------------------------------------------------------
    // Getters and Setters (Encapsulation)
    // -------------------------------------------------------
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
