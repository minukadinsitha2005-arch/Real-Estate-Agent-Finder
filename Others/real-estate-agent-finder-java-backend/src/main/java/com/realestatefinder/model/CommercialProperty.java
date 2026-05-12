package com.realestatefinder.model;

import com.realestatefinder.model.enums.PropertyType;

public class CommercialProperty extends Property {
    public CommercialProperty() {
        setPropertyType(PropertyType.COMMERCIAL);
    }

    @Override
    public String getCategoryLabel() {
        return "Commercial";
    }

    @Override
    public String getDisplayBadge() {
        return "Commercial";
    }
}
