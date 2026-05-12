package com.realestatefinder.model;

import com.realestatefinder.model.enums.PropertyType;

public class ResidentialProperty extends Property {
    public ResidentialProperty() {
        setPropertyType(PropertyType.HOUSE);
    }

    @Override
    public String getCategoryLabel() {
        return "Residential";
    }

    @Override
    public String getDisplayBadge() {
        return getPropertyType().name().replace('_', ' ');
    }
}
