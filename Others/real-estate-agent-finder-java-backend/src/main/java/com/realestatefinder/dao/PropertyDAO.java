package com.realestatefinder.dao;

import com.realestatefinder.model.Property;
import com.realestatefinder.model.enums.PropertyType;
import com.realestatefinder.util.DBUtil;
import com.realestatefinder.util.PropertyFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PropertyDAO implements CrudRepository<Property, Integer> {
    @Override
    public boolean create(Property property) throws SQLException {
        String sql = "INSERT INTO properties (title, property_type, city, price, bedrooms, bathrooms, property_size, status, agent_id, image_url, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(property, statement);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        property.setPropertyId(keys.getInt(1));
                    }
                }
            }
            return rows > 0;
        }
    }

    @Override
    public Optional<Property> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM properties WHERE property_id = ?";
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
    public List<Property> findAll() throws SQLException {
        List<Property> properties = new ArrayList<>();
        String sql = "SELECT * FROM properties ORDER BY created_at DESC";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                properties.add(mapRow(resultSet));
            }
        }
        return properties;
    }

    public List<Property> search(String keyword, String city, String type) throws SQLException {
        List<Property> properties = new ArrayList<>();
        String sql = "SELECT * FROM properties WHERE (? = '' OR title LIKE ? OR description LIKE ?) AND (? = '' OR city = ?) AND (? = '' OR property_type = ?) ORDER BY created_at DESC";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String safeKeyword = keyword == null ? "" : keyword.trim();
            String safeCity = city == null ? "" : city.trim();
            String safeType = type == null ? "" : type.trim().toUpperCase();
            statement.setString(1, safeKeyword);
            statement.setString(2, "%" + safeKeyword + "%");
            statement.setString(3, "%" + safeKeyword + "%");
            statement.setString(4, safeCity);
            statement.setString(5, safeCity);
            statement.setString(6, safeType);
            statement.setString(7, safeType);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    properties.add(mapRow(resultSet));
                }
            }
        }
        return properties;
    }

    @Override
    public boolean update(Property property) throws SQLException {
        String sql = "UPDATE properties SET title = ?, property_type = ?, city = ?, price = ?, bedrooms = ?, bathrooms = ?, property_size = ?, status = ?, agent_id = ?, image_url = ?, description = ? WHERE property_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(property, statement);
            statement.setInt(12, property.getPropertyId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM properties WHERE property_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public long countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM properties";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    private void fillStatement(Property property, PreparedStatement statement) throws SQLException {
        statement.setString(1, property.getTitle());
        statement.setString(2, property.getPropertyType().name());
        statement.setString(3, property.getCity());
        statement.setDouble(4, property.getPrice());
        statement.setInt(5, property.getBedrooms());
        statement.setInt(6, property.getBathrooms());
        statement.setString(7, property.getPropertySize());
        statement.setString(8, property.getStatus());
        if (property.getAgentId() == null) {
            statement.setNull(9, java.sql.Types.INTEGER);
        } else {
            statement.setInt(9, property.getAgentId());
        }
        statement.setString(10, property.getImageUrl());
        statement.setString(11, property.getDescription());
    }

    private Property mapRow(ResultSet resultSet) throws SQLException {
        PropertyType propertyType = PropertyType.fromValue(resultSet.getString("property_type"));
        Property property = PropertyFactory.createProperty(propertyType);
        property.setPropertyId(resultSet.getInt("property_id"));
        property.setTitle(resultSet.getString("title"));
        property.setPropertyType(propertyType);
        property.setCity(resultSet.getString("city"));
        property.setPrice(resultSet.getDouble("price"));
        property.setBedrooms(resultSet.getInt("bedrooms"));
        property.setBathrooms(resultSet.getInt("bathrooms"));
        property.setPropertySize(resultSet.getString("property_size"));
        property.setStatus(resultSet.getString("status"));
        int agentId = resultSet.getInt("agent_id");
        property.setAgentId(resultSet.wasNull() ? null : agentId);
        property.setImageUrl(resultSet.getString("image_url"));
        property.setDescription(resultSet.getString("description"));
        return property;
    }
}
