package com.realestatefinder.service;

import com.realestatefinder.dao.ServiceRequestDAO;
import com.realestatefinder.model.ServiceRequest;
import com.realestatefinder.model.enums.RequestStatus;
import com.realestatefinder.util.FileStorageUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ServiceRequestService {
    private final ServiceRequestDAO serviceRequestDAO = new ServiceRequestDAO();

    public boolean submit(ServiceRequest request) throws SQLException {
        boolean created = serviceRequestDAO.create(request);
        if (created) {
            FileStorageUtil.appendLine("service-requests.txt",
                    request.getRequestType().toDisplayText() + " | " + request.getFullName() + " | " + request.getEmail() + " | " + request.getSubject());
        }
        return created;
    }

    public List<ServiceRequest> findAll() throws SQLException {
        return serviceRequestDAO.findAll();
    }

    public Optional<ServiceRequest> findById(int requestId) throws SQLException {
        return serviceRequestDAO.findById(requestId);
    }

    public boolean updateStatus(int requestId, RequestStatus status, String actor) throws SQLException {
        boolean updated = serviceRequestDAO.updateStatus(requestId, status);
        if (updated) {
            FileStorageUtil.appendLine("admin-audit-log.txt", actor + " changed request #" + requestId + " to status " + status.name());
        }
        return updated;
    }

    public boolean delete(int requestId, String actor) throws SQLException {
        boolean deleted = serviceRequestDAO.delete(requestId);
        if (deleted) {
            FileStorageUtil.appendLine("admin-audit-log.txt", actor + " deleted service request #" + requestId);
        }
        return deleted;
    }

    public long countAll() throws SQLException {
        return serviceRequestDAO.countAll();
    }
}
