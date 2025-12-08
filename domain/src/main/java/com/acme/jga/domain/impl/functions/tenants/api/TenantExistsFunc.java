package com.acme.jga.domain.impl.functions.tenants.api;

public interface TenantExistsFunc {
    boolean existByCode(String code);

    boolean existsByExternalId(String externalId);
}
