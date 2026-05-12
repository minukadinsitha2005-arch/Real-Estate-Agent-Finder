package com.realestatefinder.servlet.admin;

import com.realestatefinder.model.enums.RequestStatus;
import com.realestatefinder.service.ServiceRequestService;
import com.realestatefinder.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/requests")
public class AdminServiceRequestServlet extends AdminBaseServlet {
    private final ServiceRequestService serviceRequestService = new ServiceRequestService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        try {
            request.setAttribute("requests", serviceRequestService.findAll());
            request.getRequestDispatcher("/WEB-INF/views/admin-requests.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load service requests.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        String action = ServletUtil.safeTrim(request.getParameter("action"));
        int requestId = ServletUtil.parseInt(request.getParameter("requestId"), 0);

        try {
            if ("delete".equalsIgnoreCase(action)) {
                serviceRequestService.delete(requestId, getActor(request));
                ServletUtil.redirectWithMessage(request, response, "/admin/requests", "success", "Request deleted successfully.");
                return;
            }
            RequestStatus status = RequestStatus.fromValue(request.getParameter("status"));
            serviceRequestService.updateStatus(requestId, status, getActor(request));
            ServletUtil.redirectWithMessage(request, response, "/admin/requests", "success", "Request status updated successfully.");
        } catch (SQLException e) {
            throw new ServletException("Failed to update request.", e);
        }
    }
}
