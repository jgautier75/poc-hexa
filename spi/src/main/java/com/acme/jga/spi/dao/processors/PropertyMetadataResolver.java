package com.acme.jga.spi.dao.processors;

import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.WrappedFunctionalException;
import com.acme.jga.domain.model.metadata.EntityMetaData;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PropertyMetadataResolver {

    public EntityMetaData resolve(Map<String, EntityMetaData> metadata, String propertyName) {
        EntityMetaData propertyMetadata = metadata.get(propertyName);
        if (propertyMetadata == null) {
            throw invalidProperty("Unmapped property named [" + propertyName + "]");
        }
        return propertyMetadata;
    }

    private WrappedFunctionalException invalidProperty(String message) {
        return new WrappedFunctionalException(
                new FunctionalException(
                        FunctionalErrors.INVALID_PROPERTY.name(),
                        null,
                        message
                )
        );
    }
}