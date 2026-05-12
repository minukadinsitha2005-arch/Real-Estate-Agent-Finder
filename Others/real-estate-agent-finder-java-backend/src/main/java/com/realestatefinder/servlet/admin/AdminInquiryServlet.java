package com.realestatefinder.servlet.admin;

import com.realestatefinder.model.enums.InquiryStatus;
import com.realestatefinder.service.ContactInquiryService;
import com.realestatefinder.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/inquiries")
public class AdminInquiryServlet extends AdminBaseServlet {
    private final ContactInquiryService contactInquiryService = new ContactInquiryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        try {
            request.setAttribute("inquiries", contactInquiryService.findAll());
            request.getRequestDispatcher("/WEB-INF/views/admin-inquiries.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load inquiries.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        String action = ServletUtil.safeTrim(request.getParameter("action"));
        int inquiryId = ServletUtil.parseInt(request.getParameter("inquiryId"), 0);

        try {
            if ("delete".equalsIgnoreCase(action)) {
                contactInquiryService.delete(inquiryId, getActor(request));
                ServletUtil.redirectWithMessage(request, response, "/admin/inquiries", "success", "Inquiry deleted successfully.");
                return;
            }
            InquiryStatus status = InquiryStatus.fromValue(request.getParameter("status"));
            contactInquiryService.updateStatus(inquiryId, status, getActor(request));
            ServletUtil.redirectWithMessage(request, response, "/admin/inquiries", "success", "Inquiry status updated successfully.");
        } catch (SQLException e) {
            throw new ServletException("Failed to update inquiry.", e);
        }
    }
}
