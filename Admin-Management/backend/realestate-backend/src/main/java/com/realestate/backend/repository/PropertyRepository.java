package com.realestate.backend.repository;

import com.realestate.backend.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByLocationContainingIgnoreCase(String location);

    List<Property> findByTypeContainingIgnoreCase(String type);

    List<Property> findByTitleContainingIgnoreCase(String keyword);
}
