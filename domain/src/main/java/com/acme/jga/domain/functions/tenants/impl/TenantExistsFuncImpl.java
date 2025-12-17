package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;

@DomainService
public class TenantExistsFuncImpl implements TenantExistsInput {
    private final TenantExistsOutput tenantExistsOutput;

    public TenantExistsFuncImpl(TenantExistsOutput tenantExistsOutput) {
        this.tenantExistsOutput = tenantExistsOutput;
    }

    @Override
    public boolean existsByCode(String code) {
        return tenantExistsOutput.existsByCode(code);
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        return tenantExistsOutput.existsByExternalId(externalId);
    }
}
