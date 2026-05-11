package com.realestate.backend.controller;

import com.realestate.backend.model.Property;
import com.realestate.backend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST Controller for Property API
// All endpoints start with /api/properties
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    // GET /api/properties  OR  ?location=  OR  ?type=  OR  ?keyword=
    @GetMapping
    public ResponseEntity<List<Property>> getProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword) {

        if (location != null) {
            return ResponseEntity.ok(propertyService.searchByLocation(location));
        }
        if (type != null) {
            return ResponseEntity.ok(propertyService.searchByType(type));
        }
        if (keyword != null) {
            return ResponseEntity.ok(propertyService.searchByKeyword(keyword));
        }
        return ResponseEntity.ok(propertyService.getAll());
    }

    // GET /api/properties/search?location=  OR  ?type=  OR  ?keyword=
    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword) {

        if (location != null) {
            return ResponseEntity.ok(propertyService.searchByLocation(location));
        }
        if (type != null) {
            return ResponseEntity.ok(propertyService.searchByType(type));
        }
        if (keyword != null) {
            return ResponseEntity.ok(propertyService.searchByKeyword(keyword));
        }
        return ResponseEntity.ok(propertyService.getAll());
    }

    // GET /api/properties/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        return propertyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/properties
    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        Property saved = propertyService.save(property);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/properties/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody Property property) {
        try {
            Property updated = propertyService.update(id, property);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/properties/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        boolean deleted = propertyService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Property deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
