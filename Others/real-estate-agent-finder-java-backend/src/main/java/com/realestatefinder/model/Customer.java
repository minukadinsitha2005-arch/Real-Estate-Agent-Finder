package com.realestatefinder.model;

import com.realestatefinder.model.enums.UserRole;

public class Customer extends AbstractUser {
    public Customer() {
        setRole(UserRole.CUSTOMER);
    }

    public Customer(int id, String fullName, String email, String phone, String passwordHash) {
        super(id, fullName, email, phone, passwordHash, UserRole.CUSTOMER);
    }

    @Override
    public String getDashboardPath() {
        return "/index.html";
    }

    @Override
    public String describePermissions() {
        return "Can browse agents, search properties, and submit service requests.";
    }
}
