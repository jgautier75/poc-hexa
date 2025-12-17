package com.acme.jga.domain.output.functions.tenants;

public interface TenantExistsInput {
    boolean existsByCode(String code);

    boolean existsByExternalId(String externalId);
}
