<%@ page import="java.util.List" %>
<%@ page import="com.realestatefinder.model.ServiceRequest" %>
<%@ page import="com.realestatefinder.model.enums.RequestStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    List<ServiceRequest> requests = (List<ServiceRequest>) request.getAttribute("requests");
    String status = request.getParameter("status");
    String message = request.getParameter("message");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Service Requests - Real Estate Agent Finder</title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body class="admin-page">
<div class="admin-wrapper">
    <div class="admin-toolbar">
        <div>
            <div class="appointment-tag">Service Request Management</div>
            <h1 class="admin-page-title">Review bookings, complaints, and support requests</h1>
            <p class="admin-subtitle">These records are created from the public appointments/support form and can be updated or deleted here.</p>
        </div>
        <div class="admin-nav">
            <a href="<%= ctx %>/admin/dashboard">Dashboard</a>
            <a href="<%= ctx %>/admin/agents">Agents</a>
            <a href="<%= ctx %>/admin/properties">Properties</a>
            <a href="<%= ctx %>/admin/requests" class="active">Requests</a>
            <a href="<%= ctx %>/admin/inquiries">Inquiries</a>
            <a href="<%= ctx %>/logout">Logout</a>
        </div>
    </div>

    <% if (message != null) { %>
        <div class="flash-message <%= "success".equalsIgnoreCase(status) ? "success" : "error" %>"><%= message %></div>
    <% } %>

    <div class="admin-card">
        <h2 class="section-title">Service request records</h2>
        <div class="admin-table-wrapper">
            <table class="admin-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Customer</th>
                    <th>Type</th>
                    <th>Preferred Schedule</th>
                    <th>Subject</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% if (requests == null || requests.isEmpty()) { %>
                    <tr><td colspan="7">No service requests submitted yet.</td></tr>
                <% } else { %>
                    <% for (ServiceRequest serviceRequest : requests) { %>
                        <tr>
                            <td><%= serviceRequest.getRequestId() %></td>
                            <td>
                                <strong><%= serviceRequest.getFullName() %></strong><br>
                                <span class="helper-text"><%= serviceRequest.getEmail() %><br><%= serviceRequest.getCity() %></span>
                            </td>
                            <td><%= serviceRequest.getRequestType().toDisplayText() %></td>
                            <td><%= serviceRequest.getPreferredDate() %> <%= serviceRequest.getPreferredTime() %></td>
                            <td>
                                <strong><%= serviceRequest.getSubject() %></strong><br>
                                <span class="helper-text"><%= serviceRequest.getMessage() %></span>
                            </td>
                            <td>
                                <span class="status-badge status-<%= serviceRequest.getStatus().name().toLowerCase() %>"><%= serviceRequest.getStatus().name() %></span>
                            </td>
                            <td>
                                <div class="admin-inline-form">
                                    <form action="<%= ctx %>/admin/requests" method="post" style="display:inline-flex; gap: 8px; align-items:center;">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="requestId" value="<%= serviceRequest.getRequestId() %>">
                                        <select name="status">
                                            <% for (RequestStatus requestStatus : RequestStatus.values()) { %>
                                                <option value="<%= requestStatus.name() %>" <%= requestStatus == serviceRequest.getStatus() ? "selected" : "" %>><%= requestStatus.name() %></option>
                                            <% } %>
                                        </select>
                                        <button type="submit" class="admin-primary-btn">Update</button>
                                    </form>
                                    <form action="<%= ctx %>/admin/requests" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="requestId" value="<%= serviceRequest.getRequestId() %>">
                                        <button type="submit" class="admin-danger-btn" onclick="return confirm('Delete this service request?');">Delete</button>
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
