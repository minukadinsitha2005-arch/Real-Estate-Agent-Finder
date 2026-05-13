package com.realestate.backend.repository;

import com.realestate.backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByCityContainingIgnoreCase(String city);
    List<Company> findByNameContainingIgnoreCase(String name);
}
