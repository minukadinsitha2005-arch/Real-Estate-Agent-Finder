package com.realestate.backend.service;

import com.realestate.backend.model.PropertyViewingRequest;
import com.realestate.backend.repository.PropertyViewingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyViewingService implements CrudService<PropertyViewingRequest, Long> {
    @Autowired
    private PropertyViewingRepository repository;

    public List<PropertyViewingRequest> getAll() { return repository.findAll(); }
    public Optional<PropertyViewingRequest> getById(Long id) { return repository.findById(id); }
    public PropertyViewingRequest save(PropertyViewingRequest request) { return repository.save(request); }

    public PropertyViewingRequest update(Long id, PropertyViewingRequest updated) {
        PropertyViewingRequest existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Viewing request not found"));
        existing.setCustomerName(updated.getCustomerName());
        existing.setCustomerEmail(updated.getCustomerEmail());
        existing.setCustomerPhone(updated.getCustomerPhone());
        existing.setSelectedProperty(updated.getSelectedProperty());
        existing.setPreferredViewingDate(updated.getPreferredViewingDate());
        existing.setMessage(updated.getMessage());
        existing.setStatus(updated.getStatus());
        return repository.save(existing);
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
