package com.realestatefinder.servlet;

import com.realestatefinder.model.Property;
import com.realestatefinder.service.PropertyService;
import com.realestatefinder.util.ServletUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/properties")
public class ApiPropertyServlet extends HttpServlet {
    private final PropertyService propertyService = new PropertyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("q");
        String city = request.getParameter("city");
        String type = request.getParameter("type");

        try {
            List<Property> properties = propertyService.search(keyword, city, type);
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < properties.size(); i++) {
                Property property = properties.get(i);
                if (i > 0) {
                    json.append(',');
                }
                json.append('{')
                    .append("\"id\":").append(property.getPropertyId()).append(',')
                    .append("\"title\":\"").append(ServletUtil.escapeJson(property.getTitle())).append("\",")
                    .append("\"type\":\"").append(property.getPropertyType().name()).append("\",")
                    .append("\"city\":\"").append(ServletUtil.escapeJson(property.getCity())).append("\",")
                    .append("\"price\":").append(property.getPrice()).append(',')
                    .append("\"bedrooms\":").append(property.getBedrooms()).append(',')
                    .append("\"bathrooms\":").append(property.getBathrooms()).append(',')
                    .append("\"propertySize\":\"").append(ServletUtil.escapeJson(property.getPropertySize())).append("\",")
                    .append("\"status\":\"").append(ServletUtil.escapeJson(property.getStatus())).append("\",")
                    .append("\"imageUrl\":\"").append(ServletUtil.escapeJson(property.getImageUrl())).append("\",")
                    .append("\"description\":\"").append(ServletUtil.escapeJson(property.getDescription())).append("\"}");
            }
            json.append(']');
            ServletUtil.writeJson(response, json.toString());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ServletUtil.writeJson(response, "{\"error\":\"Failed to load properties.\"}");
        }
    }
}
