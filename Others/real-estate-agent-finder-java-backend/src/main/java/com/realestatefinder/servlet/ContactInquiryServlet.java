package com.realestatefinder.servlet;

import com.realestatefinder.model.ContactInquiry;
import com.realestatefinder.model.enums.InquiryType;
import com.realestatefinder.service.ContactInquiryService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/contact-inquiries")
public class ContactInquiryServlet extends HttpServlet {
    private final ContactInquiryService contactInquiryService = new ContactInquiryService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        ContactInquiry inquiry = new ContactInquiry();
        inquiry.setFullName(request.getParameter("fullName"));
        inquiry.setEmail(request.getParameter("email"));
        inquiry.setPhone(request.getParameter("phone"));
        inquiry.setCity(request.getParameter("city"));
        inquiry.setInquiryType(InquiryType.fromValue(request.getParameter("inquiryType")));
        inquiry.setSubject(request.getParameter("subject"));
        inquiry.setMessage(request.getParameter("message"));

        if (inquiry.getFullName().isBlank() || inquiry.getEmail().isBlank() || inquiry.getSubject().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/contact.html?status=error&message=Missing+required+fields");
            return;
        }

        try {
            contactInquiryService.submit(inquiry);
            response.sendRedirect(request.getContextPath() + "/contact.html?contactSubmitted=1");
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/contact.html?status=error&message=Database+error");
        }
    }
}
