package com.realestate.backend.service;

import com.realestate.backend.model.Agent;
import com.realestate.backend.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// -------------------------------------------------------
// OOP CONCEPT: POLYMORPHISM
// AgentService implements CrudService<Agent, Long>.
// It provides its own version of all CRUD methods.
// -------------------------------------------------------
@Service
public class AgentService implements CrudService<Agent, Long> {

    @Autowired
    private AgentRepository agentRepository;

    // Get all agents
    @Override
    public List<Agent> getAll() {
        return agentRepository.findAll();
    }

    // Get agent by ID
    @Override
    public Optional<Agent> getById(Long id) {
        return agentRepository.findById(id);
    }

    // Save new agent
    @Override
    public Agent save(Agent agent) {
        return agentRepository.save(agent);
    }

    // Update existing agent
    @Override
    public Agent update(Long id, Agent updatedAgent) {
        Agent existing = agentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + id));

        existing.setName(updatedAgent.getName());
        existing.setEmail(updatedAgent.getEmail());
        existing.setPhone(updatedAgent.getPhone());
        existing.setLocation(updatedAgent.getLocation());
        existing.setSpecialization(updatedAgent.getSpecialization());
        existing.setExperienceYears(updatedAgent.getExperienceYears());
        existing.setImageUrl(updatedAgent.getImageUrl());
        existing.setRating(updatedAgent.getRating());
        existing.setBadge(updatedAgent.getBadge());
        existing.setDescription(updatedAgent.getDescription());

        return agentRepository.save(existing);
    }

    // Delete agent
    @Override
    public boolean delete(Long id) {
        if (agentRepository.existsById(id)) {
            agentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Search by location
    public List<Agent> searchByLocation(String location) {
        return agentRepository.findByLocationContainingIgnoreCase(location);
    }

    // Search by specialization
    public List<Agent> searchBySpecialization(String specialization) {
        return agentRepository.findBySpecializationContainingIgnoreCase(specialization);
    }
}
