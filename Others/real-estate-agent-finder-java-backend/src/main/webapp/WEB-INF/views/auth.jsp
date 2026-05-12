<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String mode = request.getAttribute("mode") == null ? "login" : request.getAttribute("mode").toString();
    boolean registerMode = "register".equalsIgnoreCase(mode);
    String error = (String) request.getAttribute("error");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= registerMode ? "Create Account" : "Log In" %> - Real Estate Agent Finder</title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body class="auth-page">
<header class="navbar">
    <div class="logo">Real Estate Agent Finder</div>
    <nav class="nav-menu">
        <a href="<%= ctx %>/index.html">Home</a>
        <a href="<%= ctx %>/agents.html">Find Agents</a>
        <a href="<%= ctx %>/properties.html">Properties</a>
        <a href="<%= ctx %>/appointments.html">Appointments</a>
        <a href="<%= ctx %>/about.html">About Us</a>
        <a href="<%= ctx %>/contact.html">Contact Us</a>
    </nav>
</header>

<div class="auth-page-container">
    <div class="auth-shell">
        <div class="auth-hero-card">
            <span class="appointment-tag">SE1020 Java Backend</span>
            <h1>Secure login and user management for your university project</h1>
            <p>
                This page gives you a working Java-based authentication flow that fits the real estate platform.
                Admin accounts can manage agents, properties, appointments, and inquiries from the dashboard,
                while customer accounts can browse and submit requests.
            </p>

            <div class="auth-feature-list">
                <div class="auth-feature-item">
                    <strong>OOP applied</strong>
                    AbstractUser, AdminUser, and Customer demonstrate abstraction, inheritance, and polymorphism.
                </div>
                <div class="auth-feature-item">
                    <strong>Database + file handling</strong>
                    Accounts are stored in MySQL, while key actions are also logged to text files for the viva.
                </div>
                <div class="auth-feature-item">
                    <strong>Demo credentials</strong>
                    Admin: <code>admin@realestate.com</code> / <code>admin123</code>
                </div>
            </div>
        </div>

        <div class="auth-form-card">
            <div class="auth-switch">
                <a href="<%= ctx %>/auth?mode=login" class="<%= !registerMode ? "active" : "" %>">Log In</a>
                <a href="<%= ctx %>/auth?mode=register" class="<%= registerMode ? "active" : "" %>">Sign Up</a>
            </div>

            <% if (error != null && !error.isBlank()) { %>
                <div class="flash-message error"><%= error %></div>
            <% } %>

            <% if (registerMode) { %>
                <form class="auth-standalone-form" action="<%= ctx %>/auth" method="post">
                    <input type="hidden" name="action" value="register">
                    <div class="form-row">
                        <div>
                            <label>Full Name</label>
                            <input type="text" name="fullName" placeholder="Enter your full name" required>
                        </div>
                        <div>
                            <label>Phone Number</label>
                            <input type="text" name="phone" placeholder="Enter your phone number">
                        </div>
                    </div>
                    <div>
                        <label>Email Address</label>
                        <input type="email" name="email" placeholder="Enter your email" required>
                    </div>
                    <div class="form-row">
                        <div>
                            <label>Password</label>
                            <input type="password" name="password" placeholder="Create a password" required>
                        </div>
                        <div>
                            <label>Confirm Password</label>
                            <input type="password" name="confirmPassword" placeholder="Confirm your password" required>
                        </div>
                    </div>
                    <button type="submit" class="continue-btn">Create Account</button>
                </form>
            <% } else { %>
                <form class="auth-standalone-form" action="<%= ctx %>/auth" method="post">
                    <input type="hidden" name="action" value="login">
                    <div>
                        <label>Email Address</label>
                        <input type="email" name="email" placeholder="Enter your email" required>
                    </div>
                    <div>
                        <label>Password</label>
                        <input type="password" name="password" placeholder="Enter your password" required>
                    </div>
                    <button type="submit" class="continue-btn">Log In</button>
                </form>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
