package com.realestatefinder.servlet.admin;

import com.realestatefinder.service.AgentService;
import com.realestatefinder.service.ContactInquiryService;
import com.realestatefinder.service.PropertyService;
import com.realestatefinder.service.ServiceRequestService;
import com.realestatefinder.util.FileStorageUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends AdminBaseServlet {
    private final AgentService agentService = new AgentService();
    private final PropertyService propertyService = new PropertyService();
    private final ServiceRequestService serviceRequestService = new ServiceRequestService();
    private final ContactInquiryService contactInquiryService = new ContactInquiryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ensureAdmin(request, response)) {
            return;
        }

        try {
            request.setAttribute("agentCount", agentService.countAll());
            request.setAttribute("propertyCount", propertyService.countAll());
            request.setAttribute("requestCount", serviceRequestService.countAll());
            request.setAttribute("inquiryCount", contactInquiryService.countAll());
            request.setAttribute("recentAuditLogs", FileStorageUtil.readLastLines("admin-audit-log.txt", 8));
            request.setAttribute("recentServiceLogs", FileStorageUtil.readLastLines("service-requests.txt", 5));
            request.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load dashboard metrics.", e);
        }
    }
}
