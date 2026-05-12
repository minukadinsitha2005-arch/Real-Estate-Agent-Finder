<%@ page import="java.util.List" %>
<%@ page import="com.realestatefinder.model.Property" %>
<%@ page import="com.realestatefinder.model.Agent" %>
<%@ page import="com.realestatefinder.model.enums.PropertyType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    List<Property> properties = (List<Property>) request.getAttribute("properties");
    List<Agent> agents = (List<Agent>) request.getAttribute("agents");
    Property editProperty = (Property) request.getAttribute("editProperty");
    boolean editing = editProperty != null;
    String status = request.getParameter("status");
    String message = request.getParameter("message");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Properties - Real Estate Agent Finder</title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body class="admin-page">
<div class="admin-wrapper">
    <div class="admin-toolbar">
        <div>
            <div class="appointment-tag">Property Management</div>
            <h1 class="admin-page-title">Create, update, and remove property listings</h1>
            <p class="admin-subtitle">This panel handles the main property records used by the real estate platform.</p>
        </div>
        <div class="admin-nav">
            <a href="<%= ctx %>/admin/dashboard">Dashboard</a>
            <a href="<%= ctx %>/admin/agents">Agents</a>
            <a href="<%= ctx %>/admin/properties" class="active">Properties</a>
            <a href="<%= ctx %>/admin/requests">Requests</a>
            <a href="<%= ctx %>/admin/inquiries">Inquiries</a>
            <a href="<%= ctx %>/logout">Logout</a>
        </div>
    </div>

    <% if (message != null) { %>
        <div class="flash-message <%= "success".equalsIgnoreCase(status) ? "success" : "error" %>"><%= message %></div>
    <% } %>

    <div class="admin-card" style="margin-bottom: 24px;">
        <h2 class="section-title"><%= editing ? "Update Property" : "Add New Property" %></h2>
        <form action="<%= ctx %>/admin/properties" method="post" class="admin-form">
            <input type="hidden" name="action" value="<%= editing ? "update" : "create" %>">
            <input type="hidden" name="propertyId" value="<%= editing ? editProperty.getPropertyId() : 0 %>">
            <div class="admin-form-grid">
                <div>
                    <label>Title</label>
                    <input type="text" name="title" value="<%= editing ? editProperty.getTitle() : "" %>" required>
                </div>
                <div>
                    <label>Property Type</label>
                    <select name="propertyType" required>
                        <% for (PropertyType propertyType : PropertyType.values()) { %>
                            <option value="<%= propertyType.name() %>" <%= editing && editProperty.getPropertyType() == propertyType ? "selected" : "" %>><%= propertyType.name() %></option>
                        <% } %>
                    </select>
                </div>
                <div>
                    <label>City</label>
                    <input type="text" name="city" value="<%= editing ? editProperty.getCity() : "" %>" required>
                </div>
                <div>
                    <label>Price</label>
                    <input type="number" step="0.01" name="price" value="<%= editing ? editProperty.getPrice() : 0 %>" required>
                </div>
                <div>
                    <label>Bedrooms</label>
                    <input type="number" name="bedrooms" value="<%= editing ? editProperty.getBedrooms() : 0 %>">
                </div>
                <div>
                    <label>Bathrooms</label>
                    <input type="number" name="bathrooms" value="<%= editing ? editProperty.getBathrooms() : 0 %>">
                </div>
                <div>
                    <label>Property Size</label>
                    <input type="text" name="propertySize" value="<%= editing ? editProperty.getPropertySize() : "" %>">
                </div>
                <div>
                    <label>Status</label>
                    <input type="text" name="status" value="<%= editing ? editProperty.getStatus() : "AVAILABLE" %>">
                </div>
                <div>
                    <label>Assigned Agent</label>
                    <select name="agentId">
                        <option value="">Not assigned</option>
                        <% if (agents != null) { %>
                            <% for (Agent agent : agents) { %>
                                <option value="<%= agent.getId() %>" <%= editing && editProperty.getAgentId() != null && editProperty.getAgentId().equals(agent.getId()) ? "selected" : "" %>><%= agent.getFullName() %></option>
                            <% } %>
                        <% } %>
                    </select>
                </div>
                <div>
                    <label>Image URL</label>
                    <input type="text" name="imageUrl" value="<%= editing ? editProperty.getImageUrl() : "" %>">
                </div>
                <div style="grid-column: 1 / -1;">
                    <label>Description</label>
                    <textarea name="description" rows="4"><%= editing ? editProperty.getDescription() : "" %></textarea>
                </div>
            </div>
            <div style="margin-top: 18px; display: flex; gap: 12px; flex-wrap: wrap;">
                <button type="submit" class="admin-primary-btn"><%= editing ? "Update Property" : "Save Property" %></button>
                <% if (editing) { %>
                    <a href="<%= ctx %>/admin/properties" class="admin-outline-btn">Clear Form</a>
                <% } %>
            </div>
        </form>
    </div>

    <div class="admin-card">
        <h2 class="section-title">Property records</h2>
        <div class="admin-table-wrapper">
            <table class="admin-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Type</th>
                    <th>City</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% if (properties == null || properties.isEmpty()) { %>
                    <tr><td colspan="7">No properties found.</td></tr>
                <% } else { %>
                    <% for (Property property : properties) { %>
                        <tr>
                            <td><%= property.getPropertyId() %></td>
                            <td>
                                <strong><%= property.getTitle() %></strong><br>
                                <span class="helper-text"><%= property.getDescription() %></span>
                            </td>
                            <td><%= property.getPropertyType().name() %></td>
                            <td><%= property.getCity() %></td>
                            <td>Rs. <%= String.format("%,.2f", property.getPrice()) %></td>
                            <td><span class="status-badge status-confirmed"><%= property.getStatus() %></span></td>
                            <td>
                                <div class="admin-inline-form">
                                    <a href="<%= ctx %>/admin/properties?editId=<%= property.getPropertyId() %>" class="admin-outline-btn">Edit</a>
                                    <form action="<%= ctx %>/admin/properties" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="propertyId" value="<%= property.getPropertyId() %>">
                                        <input type="hidden" name="id" value="<%= property.getPropertyId() %>">
                                        <button type="submit" class="admin-danger-btn" onclick="return confirm('Delete this property?');">Delete</button>
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
