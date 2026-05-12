package com.realestatefinder.servlet;

import com.realestatefinder.model.AbstractUser;
import com.realestatefinder.model.Customer;
import com.realestatefinder.service.AuthService;
import com.realestatefinder.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = ServletUtil.safeTrim(request.getParameter("mode"));
        if (mode.isBlank()) {
            mode = "login";
        }
        request.setAttribute("mode", mode);
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = ServletUtil.safeTrim(request.getParameter("action"));

        try {
            if ("register".equalsIgnoreCase(action)) {
                handleRegister(request, response);
            } else {
                handleLogin(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.setAttribute("mode", "register".equalsIgnoreCase(action) ? "register" : "login");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        Customer customer = new Customer();
        customer.setFullName(request.getParameter("fullName"));
        customer.setEmail(request.getParameter("email"));
        customer.setPhone(request.getParameter("phone"));
        String password = ServletUtil.safeTrim(request.getParameter("password"));
        String confirmPassword = ServletUtil.safeTrim(request.getParameter("confirmPassword"));

        if (customer.getFullName().isBlank() || customer.getEmail().isBlank() || password.isBlank()) {
            request.setAttribute("error", "Full name, email, and password are required.");
            request.setAttribute("mode", "register");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Password and confirm password must match.");
            request.setAttribute("mode", "register");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        boolean created = authService.registerCustomer(customer, password);
        if (created) {
            response.sendRedirect(request.getContextPath() + "/index.html?auth=registered");
        } else {
            request.setAttribute("error", "Unable to create account right now.");
            request.setAttribute("mode", "register");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String email = ServletUtil.safeTrim(request.getParameter("email"));
        String password = ServletUtil.safeTrim(request.getParameter("password"));

        Optional<AbstractUser> user = authService.login(email, password);
        if (user.isPresent()) {
            request.getSession().setAttribute("loggedInUser", user.get());
            if (user.get().isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.html");
            }
            return;
        }

        request.setAttribute("error", "Invalid email or password.");
        request.setAttribute("mode", "login");
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
    }
}
