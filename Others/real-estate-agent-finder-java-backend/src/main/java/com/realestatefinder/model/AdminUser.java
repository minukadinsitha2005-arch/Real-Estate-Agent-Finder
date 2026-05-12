package com.realestatefinder.model;

import com.realestatefinder.model.enums.UserRole;

public class AdminUser extends AbstractUser {
    public AdminUser() {
        setRole(UserRole.ADMIN);
    }

    public AdminUser(int id, String fullName, String email, String phone, String passwordHash) {
        super(id, fullName, email, phone, passwordHash, UserRole.ADMIN);
    }

    @Override
    public String getDashboardPath() {
        return "/admin/dashboard";
    }

    @Override
    public String describePermissions() {
        return "Can manage agents, properties, service requests, and contact inquiries.";
    }
}
