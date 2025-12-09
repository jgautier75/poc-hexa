package com.acme.jga.domain.functions.tenants.api;

public interface TenantExistsFunc {

    boolean existsByCode(String code);

    boolean existsByExternalId(String externalId);
}
