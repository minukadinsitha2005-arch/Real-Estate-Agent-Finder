package com.realestate.backend.service;

import java.util.List;
import java.util.Optional;

// -------------------------------------------------------
// OOP CONCEPT: POLYMORPHISM
// CrudService is a generic interface.
// All service classes implement this same interface.
// Each service provides its own version of these methods.
// That is polymorphism - same method names, different behavior.
//
// T = the entity type (Agent, Property, etc.)
// ID = the primary key type (Long)
// -------------------------------------------------------
public interface CrudService<T, ID> {

    List<T> getAll();

    Optional<T> getById(ID id);

    T save(T object);

    T update(ID id, T object);

    boolean delete(ID id);
}
