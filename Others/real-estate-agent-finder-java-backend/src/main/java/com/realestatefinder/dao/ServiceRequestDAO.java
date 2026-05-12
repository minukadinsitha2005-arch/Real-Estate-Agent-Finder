package com.realestatefinder.dao;

import com.realestatefinder.model.ServiceRequest;
import com.realestatefinder.model.enums.RequestStatus;
import com.realestatefinder.model.enums.RequestType;
import com.realestatefinder.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceRequestDAO implements CrudRepository<ServiceRequest, Integer> {
    @Override
    public boolean create(ServiceRequest request) throws SQLException {
        String sql = "INSERT INTO service_requests (full_name, email, phone, city, request_type, agent_company_name, preferred_date, preferred_time, subject, message, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(request, statement);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        request.setRequestId(keys.getInt(1));
                    }
                }
            }
            return rows > 0;
        }
    }

    @Override
    public Optional<ServiceRequest> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM service_requests WHERE request_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ServiceRequest> findAll() throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM service_requests ORDER BY created_at DESC";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                requests.add(mapRow(resultSet));
            }
        }
        return requests;
    }

    @Override
    public boolean update(ServiceRequest request) throws SQLException {
        String sql = "UPDATE service_requests SET full_name = ?, email = ?, phone = ?, city = ?, request_type = ?, agent_company_name = ?, preferred_date = ?, preferred_time = ?, subject = ?, message = ?, status = ? WHERE request_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(request, statement);
            statement.setInt(12, request.getRequestId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM service_requests WHERE request_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(int requestId, RequestStatus status) throws SQLException {
        String sql = "UPDATE service_requests SET status = ? WHERE request_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, requestId);
            return statement.executeUpdate() > 0;
        }
    }

    public long countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM service_requests";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    private void fillStatement(ServiceRequest request, PreparedStatement statement) throws SQLException {
        statement.setString(1, request.getFullName());
        statement.setString(2, request.getEmail());
        statement.setString(3, request.getPhone());
        statement.setString(4, request.getCity());
        statement.setString(5, request.getRequestType().name());
        statement.setString(6, request.getAgentCompanyName());
        statement.setString(7, request.getPreferredDate());
        statement.setString(8, request.getPreferredTime());
        statement.setString(9, request.getSubject());
        statement.setString(10, request.getMessage());
        statement.setString(11, request.getStatus().name());
    }

    private ServiceRequest mapRow(ResultSet resultSet) throws SQLException {
        ServiceRequest request = new ServiceRequest();
        request.setRequestId(resultSet.getInt("request_id"));
        request.setFullName(resultSet.getString("full_name"));
        request.setEmail(resultSet.getString("email"));
        request.setPhone(resultSet.getString("phone"));
        request.setCity(resultSet.getString("city"));
        request.setRequestType(RequestType.fromValue(resultSet.getString("request_type")));
        request.setAgentCompanyName(resultSet.getString("agent_company_name"));
        request.setPreferredDate(resultSet.getString("preferred_date"));
        request.setPreferredTime(resultSet.getString("preferred_time"));
        request.setSubject(resultSet.getString("subject"));
        request.setMessage(resultSet.getString("message"));
        request.setStatus(RequestStatus.fromValue(resultSet.getString("status")));
        var timestamp = resultSet.getTimestamp("created_at");
        if (timestamp != null) {
            request.setCreatedAt(timestamp.toLocalDateTime());
        }
        return request;
    }
}
