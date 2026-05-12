<%@ page import="java.util.List" %>
<%@ page import="com.realestatefinder.model.ContactInquiry" %>
<%@ page import="com.realestatefinder.model.enums.InquiryStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    List<ContactInquiry> inquiries = (List<ContactInquiry>) request.getAttribute("inquiries");
    String status = request.getParameter("status");
    String message = request.getParameter("message");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Contact Inquiries - Real Estate Agent Finder</title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body class="admin-page">
<div class="admin-wrapper">
    <div class="admin-toolbar">
        <div>
            <div class="appointment-tag">Contact Inquiry Management</div>
            <h1 class="admin-page-title">Review messages from the Contact Us page</h1>
            <p class="admin-subtitle">Users can create inquiries from the public contact form and admins can manage them here.</p>
        </div>
        <div class="admin-nav">
            <a href="<%= ctx %>/admin/dashboard">Dashboard</a>
            <a href="<%= ctx %>/admin/agents">Agents</a>
            <a href="<%= ctx %>/admin/properties">Properties</a>
            <a href="<%= ctx %>/admin/requests">Requests</a>
            <a href="<%= ctx %>/admin/inquiries" class="active">Inquiries</a>
            <a href="<%= ctx %>/logout">Logout</a>
        </div>
    </div>

    <% if (message != null) { %>
        <div class="flash-message <%= "success".equalsIgnoreCase(status) ? "success" : "error" %>"><%= message %></div>
    <% } %>

    <div class="admin-card">
        <h2 class="section-title">Contact inquiry records</h2>
        <div class="admin-table-wrapper">
            <table class="admin-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Sender</th>
                    <th>Inquiry Type</th>
                    <th>Subject</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% if (inquiries == null || inquiries.isEmpty()) { %>
                    <tr><td colspan="6">No inquiries found.</td></tr>
                <% } else { %>
                    <% for (ContactInquiry inquiry : inquiries) { %>
                        <tr>
                            <td><%= inquiry.getInquiryId() %></td>
                            <td>
                                <strong><%= inquiry.getFullName() %></strong><br>
                                <span class="helper-text"><%= inquiry.getEmail() %><br><%= inquiry.getCity() %></span>
                            </td>
                            <td><%= inquiry.getInquiryType().toDisplayText() %></td>
                            <td>
                                <strong><%= inquiry.getSubject() %></strong><br>
                                <span class="helper-text"><%= inquiry.getMessage() %></span>
                            </td>
                            <td><span class="status-badge status-<%= inquiry.getStatus().name().toLowerCase() %>"><%= inquiry.getStatus().name() %></span></td>
                            <td>
                                <div class="admin-inline-form">
                                    <form action="<%= ctx %>/admin/inquiries" method="post" style="display:inline-flex; gap: 8px; align-items:center;">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="inquiryId" value="<%= inquiry.getInquiryId() %>">
                                        <select name="status">
                                            <% for (InquiryStatus inquiryStatus : InquiryStatus.values()) { %>
                                                <option value="<%= inquiryStatus.name() %>" <%= inquiryStatus == inquiry.getStatus() ? "selected" : "" %>><%= inquiryStatus.name() %></option>
                                            <% } %>
                                        </select>
                                        <button type="submit" class="admin-primary-btn">Update</button>
                                    </form>
                                    <form action="<%= ctx %>/admin/inquiries" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="inquiryId" value="<%= inquiry.getInquiryId() %>">
                                        <button type="submit" class="admin-danger-btn" onclick="return confirm('Delete this inquiry?');">Delete</button>
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
