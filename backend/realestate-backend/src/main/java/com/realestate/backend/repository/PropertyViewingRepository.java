package com.realestate.backend.repository;

import com.realestate.backend.model.PropertyViewingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyViewingRepository extends JpaRepository<PropertyViewingRequest, Long> {
}
