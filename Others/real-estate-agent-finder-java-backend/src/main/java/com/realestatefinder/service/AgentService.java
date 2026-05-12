package com.realestatefinder.service;

import com.realestatefinder.dao.AgentDAO;
import com.realestatefinder.model.Agent;
import com.realestatefinder.util.FileStorageUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AgentService {
    private final AgentDAO agentDAO = new AgentDAO();

    public boolean save(Agent agent, String actor) throws SQLException {
        boolean success;
        if (agent.getId() > 0) {
            success = agentDAO.update(agent);
            if (success) {
                FileStorageUtil.appendLine("admin-audit-log.txt", actor + " updated agent #" + agent.getId() + " (" + agent.getFullName() + ")");
            }
        } else {
            success = agentDAO.create(agent);
            if (success) {
                FileStorageUtil.appendLine("admin-audit-log.txt", actor + " created agent #" + agent.getId() + " (" + agent.getFullName() + ")");
            }
        }
        return success;
    }

    public boolean delete(int agentId, String actor) throws SQLException {
        Optional<Agent> existing = agentDAO.findById(agentId);
        boolean success = agentDAO.delete(agentId);
        if (success) {
            FileStorageUtil.appendLine("admin-audit-log.txt", actor + " deleted agent #" + agentId + existing.map(agent -> " (" + agent.getFullName() + ")").orElse(""));
        }
        return success;
    }

    public List<Agent> findAll() throws SQLException {
        return agentDAO.findAll();
    }

    public List<Agent> search(String keyword, String city, String specialty) throws SQLException {
        return agentDAO.search(keyword, city, specialty);
    }

    public Optional<Agent> findById(int agentId) throws SQLException {
        return agentDAO.findById(agentId);
    }

    public long countAll() throws SQLException {
        return agentDAO.countAll();
    }
}
