package com.realestatefinder.servlet.admin;

import com.realestatefinder.model.Property;
import com.realestatefinder.model.enums.PropertyType;
import com.realestatefinder.service.AgentService;
import com.realestatefinder.service.PropertyService;
import com.realestatefinder.util.PropertyFactory;
import com.realestatefinder.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/properties")
public class AdminPropertyServlet extends AdminBaseServlet {
    private final PropertyService propertyService = new PropertyService();
    private final AgentService agentService = new AgentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        try {
            String editId = request.getParameter("editId");
            if (editId != null && !editId.isBlank()) {
                propertyService.findById(Integer.parseInt(editId)).ifPresent(property -> request.setAttribute("editProperty", property));
            }
            request.setAttribute("properties", propertyService.findAll());
            request.setAttribute("agents", agentService.findAll());
            request.getRequestDispatcher("/WEB-INF/views/admin-properties.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load properties.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String action = ServletUtil.safeTrim(request.getParameter("action"));

        try {
            if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                propertyService.delete(id, getActor(request));
                ServletUtil.redirectWithMessage(request, response, "/admin/properties", "success", "Property deleted successfully.");
                return;
            }

            PropertyType propertyType = PropertyType.fromValue(request.getParameter("propertyType"));
            Property property = PropertyFactory.createProperty(propertyType);
            property.setPropertyId(ServletUtil.parseInt(request.getParameter("propertyId"), 0));
            property.setTitle(request.getParameter("title"));
            property.setPropertyType(propertyType);
            property.setCity(request.getParameter("city"));
            property.setPrice(ServletUtil.parseDouble(request.getParameter("price"), 0));
            property.setBedrooms(ServletUtil.parseInt(request.getParameter("bedrooms"), 0));
            property.setBathrooms(ServletUtil.parseInt(request.getParameter("bathrooms"), 0));
            property.setPropertySize(request.getParameter("propertySize"));
            property.setStatus(request.getParameter("status"));
            property.setAgentId(ServletUtil.parseIntegerObject(request.getParameter("agentId")));
            property.setImageUrl(request.getParameter("imageUrl"));
            property.setDescription(request.getParameter("description"));

            propertyService.save(property, getActor(request));
            ServletUtil.redirectWithMessage(request, response, "/admin/properties", "success", "Property saved successfully.");
        } catch (SQLException e) {
            throw new ServletException("Failed to save property.", e);
        }
    }
}
