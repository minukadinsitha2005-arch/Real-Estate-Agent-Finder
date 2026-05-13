package com.realestate.backend.service;

import com.realestate.backend.model.Company;
import com.realestate.backend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService implements CrudService<Company, Long> {
    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> getAll() { return companyRepository.findAll(); }
    public Optional<Company> getById(Long id) { return companyRepository.findById(id); }
    public Company save(Company company) { return companyRepository.save(company); }

    public Company update(Long id, Company updated) {
        Company existing = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        existing.setName(updated.getName());
        existing.setCity(updated.getCity());
        existing.setAddress(updated.getAddress());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setImageUrl(updated.getImageUrl());
        existing.setBadge(updated.getBadge());
        existing.setDescription(updated.getDescription());
        return companyRepository.save(existing);
    }

    public boolean delete(Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Company> searchByCity(String city) { return companyRepository.findByCityContainingIgnoreCase(city); }
    public List<Company> searchByName(String name) { return companyRepository.findByNameContainingIgnoreCase(name); }
}
