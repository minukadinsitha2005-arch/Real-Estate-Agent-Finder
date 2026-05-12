<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    long agentCount = request.getAttribute("agentCount") == null ? 0L : (Long) request.getAttribute("agentCount");
    long propertyCount = request.getAttribute("propertyCount") == null ? 0L : (Long) request.getAttribute("propertyCount");
    long requestCount = request.getAttribute("requestCount") == null ? 0L : (Long) request.getAttribute("requestCount");
    long inquiryCount = request.getAttribute("inquiryCount") == null ? 0L : (Long) request.getAttribute("inquiryCount");
    List<String> recentAuditLogs = (List<String>) request.getAttribute("recentAuditLogs");
    List<String> recentServiceLogs = (List<String>) request.getAttribute("recentServiceLogs");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Real Estate Agent Finder</title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body class="admin-page">
<div class="admin-wrapper">
    <div class="admin-toolbar">
        <div>
            <div class="appointment-tag">Admin Panel</div>
            <h1 class="admin-page-title">Real Estate Agent Finder Dashboard</h1>
            <p class="admin-subtitle">Manage the backend data required for your SE1020 project and demonstrate CRUD, OOP, database access, and file handling from one place.</p>
        </div>
        <div class="admin-nav">
            <a href="<%= ctx %>/admin/dashboard" class="active">Dashboard</a>
            <a href="<%= ctx %>/admin/agents">Agents</a>
            <a href="<%= ctx %>/admin/properties">Properties</a>
            <a href="<%= ctx %>/admin/requests">Requests</a>
            <a href="<%= ctx %>/admin/inquiries">Inquiries</a>
            <a href="<%= ctx %>/logout">Logout</a>
        </div>
    </div>

    <div class="dashboard-metrics">
        <div class="dashboard-metric">
            <h3><%= agentCount %></h3>
            <p class="metric-subtitle">Agents stored in MySQL</p>
        </div>
        <div class="dashboard-metric">
            <h3><%= propertyCount %></h3>
            <p class="metric-subtitle">Properties available in the system</p>
        </div>
        <div class="dashboard-metric">
            <h3><%= requestCount %></h3>
            <p class="metric-subtitle">Service requests from the appointments page</p>
        </div>
        <div class="dashboard-metric">
            <h3><%= inquiryCount %></h3>
            <p class="metric-subtitle">Contact inquiries from the public site</p>
        </div>
    </div>

    <div class="admin-grid-2" style="margin-top: 24px;">
        <div class="admin-card">
            <h2 class="section-title">Recent admin audit log</h2>
            <p class="helper-text">This data is read from a text file in <code>WEB-INF/data/admin-audit-log.txt</code> to support the file handling requirement in the marking guide.</p>
            <div class="audit-log-list" style="margin-top: 18px;">
                <% if (recentAuditLogs == null || recentAuditLogs.isEmpty()) { %>
                    <div class="audit-log-item">No admin audit entries yet.</div>
                <% } else { %>
                    <% for (String log : recentAuditLogs) { %>
                        <div class="audit-log-item"><%= log %></div>
                    <% } %>
                <% } %>
            </div>
        </div>

        <div class="admin-card">
            <h2 class="section-title">Recent public submissions</h2>
            <p class="helper-text">These lines are read from the file backup that mirrors public appointment/support form submissions.</p>
            <div class="audit-log-list" style="margin-top: 18px;">
                <% if (recentServiceLogs == null || recentServiceLogs.isEmpty()) { %>
                    <div class="audit-log-item">No service request logs found yet.</div>
                <% } else { %>
                    <% for (String log : recentServiceLogs) { %>
                        <div class="audit-log-item"><%= log %></div>
                    <% } %>
                <% } %>
            </div>
        </div>
    </div>
</div>
</body>
</html>
