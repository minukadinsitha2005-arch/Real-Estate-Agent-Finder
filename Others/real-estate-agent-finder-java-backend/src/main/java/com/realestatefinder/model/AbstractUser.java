package com.realestatefinder.model;

import com.realestatefinder.model.enums.UserRole;

public abstract class AbstractUser extends Person {
    private String passwordHash;
    private UserRole role = UserRole.CUSTOMER;

    protected AbstractUser() {
    }

    protected AbstractUser(int id, String fullName, String email, String phone, String passwordHash, UserRole role) {
        super(id, fullName, email, phone);
        this.passwordHash = passwordHash;
        this.role = role == null ? UserRole.CUSTOMER : role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role == null ? UserRole.CUSTOMER : role;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    @Override
    public String getContactSummary() {
        return getFullName() + " | " + getEmail();
    }

    public abstract String getDashboardPath();

    public abstract String describePermissions();
}
