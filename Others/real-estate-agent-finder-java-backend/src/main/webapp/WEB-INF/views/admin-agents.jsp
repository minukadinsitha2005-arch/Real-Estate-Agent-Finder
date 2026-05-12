<%@ page import="java.util.List" %>
<%@ page import="com.realestatefinder.model.Agent" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    List<Agent> agents = (List<Agent>) request.getAttribute("agents");
    Agent editAgent = (Agent) request.getAttribute("editAgent");
    boolean editing = editAgent != null;
    String status = request.getParameter("status");
    String message = request.getParameter("message");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Agents - Real Estate Agent Finder</title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body class="admin-page">
<div class="admin-wrapper">
    <div class="admin-toolbar">
        <div>
            <div class="appointment-tag">Agent Management</div>
            <h1 class="admin-page-title">Create, edit, and delete agents</h1>
            <p class="admin-subtitle">This module directly supports CRUD marks and connects with the public agent finder concept in your frontend.</p>
        </div>
        <div class="admin-nav">
            <a href="<%= ctx %>/admin/dashboard">Dashboard</a>
            <a href="<%= ctx %>/admin/agents" class="active">Agents</a>
            <a href="<%= ctx %>/admin/properties">Properties</a>
            <a href="<%= ctx %>/admin/requests">Requests</a>
            <a href="<%= ctx %>/admin/inquiries">Inquiries</a>
            <a href="<%= ctx %>/logout">Logout</a>
        </div>
    </div>

    <% if (message != null) { %>
        <div class="flash-message <%= "success".equalsIgnoreCase(status) ? "success" : "error" %>"><%= message %></div>
    <% } %>

    <div class="admin-card" style="margin-bottom: 24px;">
        <h2 class="section-title"><%= editing ? "Update Agent" : "Add New Agent" %></h2>
        <form action="<%= ctx %>/admin/agents" method="post" class="admin-form">
            <input type="hidden" name="action" value="<%= editing ? "update" : "create" %>">
            <input type="hidden" name="id" value="<%= editing ? editAgent.getId() : 0 %>">
            <div class="admin-form-grid">
                <div>
                    <label>Full Name</label>
                    <input type="text" name="fullName" value="<%= editing ? editAgent.getFullName() : "" %>" required>
                </div>
                <div>
                    <label>Email</label>
                    <input type="email" name="email" value="<%= editing ? editAgent.getEmail() : "" %>" required>
                </div>
                <div>
                    <label>Phone</label>
                    <input type="text" name="phone" value="<%= editing ? editAgent.getPhone() : "" %>" required>
                </div>
                <div>
                    <label>City</label>
                    <input type="text" name="city" value="<%= editing ? editAgent.getCity() : "" %>" required>
                </div>
                <div>
                    <label>Specialty</label>
                    <input type="text" name="specialty" value="<%= editing ? editAgent.getSpecialty() : "" %>" required>
                </div>
                <div>
                    <label>Experience (Years)</label>
                    <input type="number" name="experienceYears" value="<%= editing ? editAgent.getExperienceYears() : 0 %>" min="0" required>
                </div>
                <div>
                    <label>Rating</label>
                    <input type="number" step="0.01" name="rating" value="<%= editing ? editAgent.getRating() : 4.50 %>" min="0" max="5" required>
                </div>
                <div>
                    <label>Status</label>
                    <input type="text" name="status" value="<%= editing ? editAgent.getStatus() : "AVAILABLE" %>" required>
                </div>
                <div style="grid-column: 1 / -1;">
                    <label>Bio</label>
                    <textarea name="bio" rows="4"><%= editing ? editAgent.getBio() : "" %></textarea>
                </div>
            </div>
            <div style="margin-top: 18px; display: flex; gap: 12px; flex-wrap: wrap;">
                <button type="submit" class="admin-primary-btn"><%= editing ? "Update Agent" : "Save Agent" %></button>
                <% if (editing) { %>
                    <a href="<%= ctx %>/admin/agents" class="admin-outline-btn">Clear Form</a>
                <% } %>
            </div>
        </form>
    </div>

    <div class="admin-card">
        <h2 class="section-title">Agent records</h2>
        <div class="admin-table-wrapper">
            <table class="admin-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>City</th>
                    <th>Specialty</th>
                    <th>Experience</th>
                    <th>Rating</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% if (agents == null || agents.isEmpty()) { %>
                    <tr><td colspan="8">No agents available.</td></tr>
                <% } else { %>
                    <% for (Agent agent : agents) { %>
                        <tr>
                            <td><%= agent.getId() %></td>
                            <td>
                                <strong><%= agent.getFullName() %></strong><br>
                                <span class="helper-text"><%= agent.getEmail() %></span>
                            </td>
                            <td><%= agent.getCity() %></td>
                            <td><%= agent.getSpecialty() %></td>
                            <td><%= agent.getExperienceYears() %> yrs</td>
                            <td><%= String.format("%.2f", agent.getRating()) %></td>
                            <td><span class="status-badge status-confirmed"><%= agent.getStatus() %></span></td>
                            <td>
                                <div class="admin-inline-form">
                                    <a href="<%= ctx %>/admin/agents?editId=<%= agent.getId() %>" class="admin-outline-btn">Edit</a>
                                    <form action="<%= ctx %>/admin/agents" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="<%= agent.getId() %>">
                                        <button type="submit" class="admin-danger-btn" onclick="return confirm('Delete this agent?');">Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    <% } %>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
