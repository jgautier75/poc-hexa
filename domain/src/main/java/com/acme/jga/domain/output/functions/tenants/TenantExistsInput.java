package com.acme.jga.domain.output.functions.tenants;

import com.acme.jga.domain.model.generic.CompositeId;

public interface TenantExistsInput {
    boolean existsByCode(String code);

    boolean existsByExternalId(String externalId);

    boolean existsById(CompositeId compositeId);
}
