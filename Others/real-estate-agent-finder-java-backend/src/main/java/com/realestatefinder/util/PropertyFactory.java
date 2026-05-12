package com.realestatefinder.util;

import com.realestatefinder.model.CommercialProperty;
import com.realestatefinder.model.Property;
import com.realestatefinder.model.ResidentialProperty;
import com.realestatefinder.model.enums.PropertyType;

public final class PropertyFactory {
    private PropertyFactory() {
    }

    public static Property createProperty(PropertyType propertyType) {
        if (propertyType == PropertyType.COMMERCIAL) {
            return new CommercialProperty();
        }
        return new ResidentialProperty();
    }
}
