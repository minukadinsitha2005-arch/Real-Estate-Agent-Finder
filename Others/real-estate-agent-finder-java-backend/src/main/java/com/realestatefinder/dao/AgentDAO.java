package com.realestatefinder.dao;

import com.realestatefinder.model.Agent;
import com.realestatefinder.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgentDAO implements CrudRepository<Agent, Integer> {
    @Override
    public boolean create(Agent agent) throws SQLException {
        String sql = "INSERT INTO agents (full_name, email, phone, city, specialty, experience_years, rating, status, bio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(agent, statement);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        agent.setId(keys.getInt(1));
                    }
                }
            }
            return rows > 0;
        }
    }

    @Override
    public Optional<Agent> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM agents WHERE agent_id = ?";
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
    public List<Agent> findAll() throws SQLException {
        List<Agent> agents = new ArrayList<>();
        String sql = "SELECT * FROM agents ORDER BY created_at DESC";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                agents.add(mapRow(resultSet));
            }
        }
        return agents;
    }

    public List<Agent> search(String keyword, String city, String specialty) throws SQLException {
        List<Agent> agents = new ArrayList<>();
        String sql = "SELECT * FROM agents WHERE (? = '' OR full_name LIKE ? OR bio LIKE ?) AND (? = '' OR city = ?) AND (? = '' OR specialty LIKE ?) ORDER BY rating DESC, experience_years DESC";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String safeKeyword = keyword == null ? "" : keyword.trim();
            String safeCity = city == null ? "" : city.trim();
            String safeSpecialty = specialty == null ? "" : specialty.trim();
            statement.setString(1, safeKeyword);
            statement.setString(2, "%" + safeKeyword + "%");
            statement.setString(3, "%" + safeKeyword + "%");
            statement.setString(4, safeCity);
            statement.setString(5, safeCity);
            statement.setString(6, safeSpecialty);
            statement.setString(7, "%" + safeSpecialty + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    agents.add(mapRow(resultSet));
                }
            }
        }
        return agents;
    }

    @Override
    public boolean update(Agent agent) throws SQLException {
        String sql = "UPDATE agents SET full_name = ?, email = ?, phone = ?, city = ?, specialty = ?, experience_years = ?, rating = ?, status = ?, bio = ? WHERE agent_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(agent, statement);
            statement.setInt(10, agent.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM agents WHERE agent_id = ?";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public long countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM agents";
        try (var connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    private void fillStatement(Agent agent, PreparedStatement statement) throws SQLException {
        statement.setString(1, agent.getFullName());
        statement.setString(2, agent.getEmail());
        statement.setString(3, agent.getPhone());
        statement.setString(4, agent.getCity());
        statement.setString(5, agent.getSpecialty());
        statement.setInt(6, agent.getExperienceYears());
        statement.setDouble(7, agent.getRating());
        statement.setString(8, agent.getStatus());
        statement.setString(9, agent.getBio());
    }

    private Agent mapRow(ResultSet resultSet) throws SQLException {
        Agent agent = new Agent();
        agent.setId(resultSet.getInt("agent_id"));
        agent.setFullName(resultSet.getString("full_name"));
        agent.setEmail(resultSet.getString("email"));
        agent.setPhone(resultSet.getString("phone"));
        agent.setCity(resultSet.getString("city"));
        agent.setSpecialty(resultSet.getString("specialty"));
        agent.setExperienceYears(resultSet.getInt("experience_years"));
        agent.setRating(resultSet.getDouble("rating"));
        agent.setStatus(resultSet.getString("status"));
        agent.setBio(resultSet.getString("bio"));
        return agent;
    }
}
