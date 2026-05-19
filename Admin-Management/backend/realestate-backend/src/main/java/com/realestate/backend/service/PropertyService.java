package com.realestate.backend.service;

import com.realestate.backend.model.Property;
import com.realestate.backend.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// OOP CONCEPT: POLYMORPHISM - implements CrudService
@Service
public class PropertyService implements CrudService<Property, Long> {

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    @Override
    public Optional<Property> getById(Long id) {
        return propertyRepository.findById(id);
    }

    @Override
    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public Property update(Long id, Property updatedProperty) {
        Property existing = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with id: " + id));

        existing.setTitle(updatedProperty.getTitle());
        existing.setType(updatedProperty.getType());
        existing.setLocation(updatedProperty.getLocation());
        existing.setPrice(updatedProperty.getPrice());
        existing.setBedrooms(updatedProperty.getBedrooms());
        existing.setBathrooms(updatedProperty.getBathrooms());
        existing.setDescription(updatedProperty.getDescription());
        existing.setImageUrl(updatedProperty.getImageUrl());
        existing.setAgentName(updatedProperty.getAgentName());
        existing.setStatus(updatedProperty.getStatus());

        return propertyRepository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (propertyRepository.existsById(id)) {
            propertyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Property> searchByLocation(String location) {
        return propertyRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Property> searchByType(String type) {
        return propertyRepository.findByTypeContainingIgnoreCase(type);
    }

    public List<Property> searchByKeyword(String keyword) {
        return propertyRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
