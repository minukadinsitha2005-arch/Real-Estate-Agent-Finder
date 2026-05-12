package com.realestatefinder.service;

import com.realestatefinder.dao.UserDAO;
import com.realestatefinder.model.AbstractUser;
import com.realestatefinder.model.Customer;
import com.realestatefinder.util.FileStorageUtil;
import com.realestatefinder.util.PasswordUtil;

import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public boolean registerCustomer(Customer customer, String plainPassword) throws SQLException {
        customer.setPasswordHash(PasswordUtil.hashPassword(plainPassword));
        boolean created = userDAO.create(customer);
        if (created) {
            FileStorageUtil.appendLine("admin-audit-log.txt", "New customer registered: " + customer.getEmail());
        }
        return created;
    }

    public Optional<AbstractUser> login(String email, String plainPassword) throws SQLException {
        Optional<AbstractUser> user = userDAO.findByEmail(email);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        String hashedInput = PasswordUtil.hashPassword(plainPassword);
        if (hashedInput.equals(user.get().getPasswordHash())) {
            return user;
        }
        return Optional.empty();
    }
}
