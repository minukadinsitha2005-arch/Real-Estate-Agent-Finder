package com.realestatefinder.service;

import com.realestatefinder.dao.PropertyDAO;
import com.realestatefinder.model.Property;
import com.realestatefinder.util.FileStorageUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PropertyService {
    private final PropertyDAO propertyDAO = new PropertyDAO();

    public boolean save(Property property, String actor) throws SQLException {
        boolean success;
        if (property.getPropertyId() > 0) {
            success = propertyDAO.update(property);
            if (success) {
                FileStorageUtil.appendLine("admin-audit-log.txt", actor + " updated property #" + property.getPropertyId() + " (" + property.getTitle() + ")");
            }
        } else {
            success = propertyDAO.create(property);
            if (success) {
                FileStorageUtil.appendLine("admin-audit-log.txt", actor + " created property #" + property.getPropertyId() + " (" + property.getTitle() + ")");
            }
        }
        return success;
    }

    public boolean delete(int propertyId, String actor) throws SQLException {
        Optional<Property> existing = propertyDAO.findById(propertyId);
        boolean success = propertyDAO.delete(propertyId);
        if (success) {
            FileStorageUtil.appendLine("admin-audit-log.txt", actor + " deleted property #" + propertyId + existing.map(property -> " (" + property.getTitle() + ")").orElse(""));
        }
        return success;
    }

    public List<Property> findAll() throws SQLException {
        return propertyDAO.findAll();
    }

    public List<Property> search(String keyword, String city, String type) throws SQLException {
        return propertyDAO.search(keyword, city, type);
    }

    public Optional<Property> findById(int propertyId) throws SQLException {
        return propertyDAO.findById(propertyId);
    }

    public long countAll() throws SQLException {
        return propertyDAO.countAll();
    }
}
