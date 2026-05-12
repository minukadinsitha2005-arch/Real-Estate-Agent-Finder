package com.realestate.backend.repository;

import com.realestate.backend.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// JpaRepository gives us built-in CRUD methods for free
@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    // Spring Data JPA auto-generates SQL from method names
    List<Agent> findByLocationContainingIgnoreCase(String location);

    List<Agent> findBySpecializationContainingIgnoreCase(String specialization);
}
