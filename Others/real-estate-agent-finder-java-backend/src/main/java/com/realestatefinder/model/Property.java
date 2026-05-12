package com.realestatefinder.model;

import com.realestatefinder.model.enums.PropertyType;

public abstract class Property {
    private int propertyId;
    private String title;
    private PropertyType propertyType;
    private String city;
    private double price;
    private int bedrooms;
    private int bathrooms;
    private String propertySize;
    private String status;
    private Integer agentId;
    private String imageUrl;
    private String description;

    protected Property() {
    }

    protected Property(int propertyId, String title, PropertyType propertyType, String city, double price,
                       int bedrooms, int bathrooms, String propertySize, String status,
                       Integer agentId, String imageUrl, String description) {
        this.propertyId = propertyId;
        this.title = title;
        this.propertyType = propertyType;
        this.city = city;
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.propertySize = propertySize;
        this.status = status;
        this.agentId = agentId;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title.trim();
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType == null ? PropertyType.HOUSE : propertyType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? "" : city.trim();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(String propertySize) {
        this.propertySize = propertySize == null ? "" : propertySize.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "AVAILABLE" : status.trim();
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? "" : imageUrl.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description.trim();
    }

    public abstract String getCategoryLabel();

    public abstract String getDisplayBadge();
}
