package com.realestate.backend.controller;

import com.realestate.backend.model.Agent;
import com.realestate.backend.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// All endpoints start with /api/agents
@RestController
@RequestMapping("/api/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    // GET /api/agents  OR  GET /api/agents?location=Colombo  OR  ?specialization=Luxury
    @GetMapping
    public ResponseEntity<List<Agent>> getAgents(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String specialization) {

        if (location != null) {
            return ResponseEntity.ok(agentService.searchByLocation(location));
        }
        if (specialization != null) {
            return ResponseEntity.ok(agentService.searchBySpecialization(specialization));
        }
        return ResponseEntity.ok(agentService.getAll());
    }

    // GET /api/agents/search?location=Colombo  OR  ?specialization=Luxury Homes
    @GetMapping("/search")
    public ResponseEntity<List<Agent>> searchAgents(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String specialization) {

        if (location != null) {
            return ResponseEntity.ok(agentService.searchByLocation(location));
        }
        if (specialization != null) {
            return ResponseEntity.ok(agentService.searchBySpecialization(specialization));
        }
        return ResponseEntity.ok(agentService.getAll());
    }

    // GET /api/agents/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable Long id) {
        return agentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/agents
    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        Agent saved = agentService.save(agent);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/agents/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(@PathVariable Long id, @RequestBody Agent agent) {
        try {
            Agent updated = agentService.update(id, agent);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/agents/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAgent(@PathVariable Long id) {
        boolean deleted = agentService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Agent deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
