package com.realestatefinder.servlet;

import com.realestatefinder.model.ServiceRequest;
import com.realestatefinder.model.enums.RequestType;
import com.realestatefinder.service.ServiceRequestService;
import com.realestatefinder.util.ServletUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/service-requests")
public class ServiceRequestServlet extends HttpServlet {
    private final ServiceRequestService serviceRequestService = new ServiceRequestService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setFullName(request.getParameter("fullName"));
        serviceRequest.setEmail(request.getParameter("email"));
        serviceRequest.setPhone(request.getParameter("phone"));
        serviceRequest.setCity(request.getParameter("city"));
        serviceRequest.setRequestType(RequestType.fromValue(request.getParameter("requestType")));
        serviceRequest.setAgentCompanyName(request.getParameter("agentCompanyName"));
        serviceRequest.setPreferredDate(request.getParameter("preferredDate"));
        serviceRequest.setPreferredTime(request.getParameter("preferredTime"));
        serviceRequest.setSubject(request.getParameter("subject"));
        serviceRequest.setMessage(request.getParameter("message"));

        if (serviceRequest.getFullName().isBlank() || serviceRequest.getEmail().isBlank() || serviceRequest.getSubject().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/appointments.html?status=error&message=Missing+required+fields");
            return;
        }

        try {
            serviceRequestService.submit(serviceRequest);
            response.sendRedirect(request.getContextPath() + "/appointments.html?submitted=1");
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/appointments.html?status=error&message=Database+error");
        }
    }
}
