package com.realestatefinder.model;

public class Agent extends Person {
    private String city;
    private String specialty;
    private int experienceYears;
    private double rating;
    private String status;
    private String bio;

    public Agent() {
    }

    public Agent(int id, String fullName, String email, String phone, String city, String specialty,
                 int experienceYears, double rating, String status, String bio) {
        super(id, fullName, email, phone);
        this.city = city;
        this.specialty = specialty;
        this.experienceYears = experienceYears;
        this.rating = rating;
        this.status = status;
        this.bio = bio;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? "" : city.trim();
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty == null ? "" : specialty.trim();
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "AVAILABLE" : status.trim();
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio == null ? "" : bio.trim();
    }

    @Override
    public String getContactSummary() {
        return getFullName() + " - " + city + " - " + specialty;
    }
}
