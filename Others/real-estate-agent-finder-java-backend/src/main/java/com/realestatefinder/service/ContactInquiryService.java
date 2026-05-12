package com.realestatefinder.service;

import com.realestatefinder.dao.ContactInquiryDAO;
import com.realestatefinder.model.ContactInquiry;
import com.realestatefinder.model.enums.InquiryStatus;
import com.realestatefinder.util.FileStorageUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ContactInquiryService {
    private final ContactInquiryDAO contactInquiryDAO = new ContactInquiryDAO();

    public boolean submit(ContactInquiry inquiry) throws SQLException {
        boolean created = contactInquiryDAO.create(inquiry);
        if (created) {
            FileStorageUtil.appendLine("contact-inquiries.txt",
                    inquiry.getInquiryType().toDisplayText() + " | " + inquiry.getFullName() + " | " + inquiry.getEmail() + " | " + inquiry.getSubject());
        }
        return created;
    }

    public List<ContactInquiry> findAll() throws SQLException {
        return contactInquiryDAO.findAll();
    }

    public Optional<ContactInquiry> findById(int inquiryId) throws SQLException {
        return contactInquiryDAO.findById(inquiryId);
    }

    public boolean updateStatus(int inquiryId, InquiryStatus status, String actor) throws SQLException {
        boolean updated = contactInquiryDAO.updateStatus(inquiryId, status);
        if (updated) {
            FileStorageUtil.appendLine("admin-audit-log.txt", actor + " changed inquiry #" + inquiryId + " to status " + status.name());
        }
        return updated;
    }

    public boolean delete(int inquiryId, String actor) throws SQLException {
        boolean deleted = contactInquiryDAO.delete(inquiryId);
        if (deleted) {
            FileStorageUtil.appendLine("admin-audit-log.txt", actor + " deleted inquiry #" + inquiryId);
        }
        return deleted;
    }

    public long countAll() throws SQLException {
        return contactInquiryDAO.countAll();
    }
}
