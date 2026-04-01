package com.acme.jga.domain.model.metadata;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class EntityMetaData {
    private final String key;
    private final String value;
    private final boolean isDiacritic;
}
