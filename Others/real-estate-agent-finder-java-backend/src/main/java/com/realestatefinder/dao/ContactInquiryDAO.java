package com.realestatefinder.dao;

import com.realestatefinder.model.ContactInquiry;
import com.realestatefinder.model.enums.InquiryStatus;
import com.realestatefinder.model.enums.InquiryType;
import com.realestatefinder.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactInquiryDAO implements CrudRepository<ContactInquiry, Integer> {
    @Override
    public boolean create(ContactInquiry inquiry) throws SQLException {
        String sql = "INSERT INTO contact_inquiries (full_name, email, phone, city, inquiry_type, subject, message, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(inquiry, statement);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        inquiry.setInquiryId(keys.getInt(1));
                    }
                }
            }
            return rows > 0;
        }
    }

    @Override
    public Optional<ContactInquiry> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM contact_inquiries WHERE inquiry_id = ?";
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
    public List<ContactInquiry> findAll() throws SQLException {
        List<ContactInquiry> inquiries = new ArrayList<>();
        String sql = "SELECT * FROM contact_inquiries ORDER BY created_at DESC";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                inquiries.add(mapRow(resultSet));
            }
        }
        return inquiries;
    }

    @Override
    public boolean update(ContactInquiry inquiry) throws SQLException {
        String sql = "UPDATE contact_inquiries SET full_name = ?, email = ?, phone = ?, city = ?, inquiry_type = ?, subject = ?, message = ?, status = ? WHERE inquiry_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(inquiry, statement);
            statement.setInt(9, inquiry.getInquiryId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM contact_inquiries WHERE inquiry_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(int inquiryId, InquiryStatus status) throws SQLException {
        String sql = "UPDATE contact_inquiries SET status = ? WHERE inquiry_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, inquiryId);
            return statement.executeUpdate() > 0;
        }
    }

    public long countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM contact_inquiries";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    private void fillStatement(ContactInquiry inquiry, PreparedStatement statement) throws SQLException {
        statement.setString(1, inquiry.getFullName());
        statement.setString(2, inquiry.getEmail());
        statement.setString(3, inquiry.getPhone());
        statement.setString(4, inquiry.getCity());
        statement.setString(5, inquiry.getInquiryType().name());
        statement.setString(6, inquiry.getSubject());
        statement.setString(7, inquiry.getMessage());
        statement.setString(8, inquiry.getStatus().name());
    }

    private ContactInquiry mapRow(ResultSet resultSet) throws SQLException {
        ContactInquiry inquiry = new ContactInquiry();
        inquiry.setInquiryId(resultSet.getInt("inquiry_id"));
        inquiry.setFullName(resultSet.getString("full_name"));
        inquiry.setEmail(resultSet.getString("email"));
        inquiry.setPhone(resultSet.getString("phone"));
        inquiry.setCity(resultSet.getString("city"));
        inquiry.setInquiryType(InquiryType.fromValue(resultSet.getString("inquiry_type")));
        inquiry.setSubject(resultSet.getString("subject"));
        inquiry.setMessage(resultSet.getString("message"));
        inquiry.setStatus(InquiryStatus.fromValue(resultSet.getString("status")));
        var timestamp = resultSet.getTimestamp("created_at");
        if (timestamp != null) {
            inquiry.setCreatedAt(timestamp.toLocalDateTime());
        }
        return inquiry;
    }
}
