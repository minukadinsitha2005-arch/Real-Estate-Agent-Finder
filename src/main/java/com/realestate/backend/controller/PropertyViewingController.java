package com.realestate.backend.controller;

import com.realestate.backend.model.PropertyViewingRequest;
import com.realestate.backend.service.PropertyViewingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property-viewings")
public class PropertyViewingController {
    @Autowired
    private PropertyViewingService service;

    @GetMapping
    public ResponseEntity<List<PropertyViewingRequest>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyViewingRequest> getById(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PropertyViewingRequest> create(@RequestBody PropertyViewingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.ok("Viewing request deleted") : ResponseEntity.notFound().build();
    }
}
