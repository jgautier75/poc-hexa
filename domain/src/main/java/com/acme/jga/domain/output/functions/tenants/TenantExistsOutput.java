package com.acme.jga.domain.output.functions.tenants;

public interface TenantExistsOutput {
    boolean existsByCode(String code);

    boolean existsByExternalId(String externalId);
}
