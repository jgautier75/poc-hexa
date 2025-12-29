package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;
import io.micrometer.observation.annotation.Observed;

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
    @Observed(name = "DOMAIN_TENANT_EXISTS_EXTERNAL_ID")
    public boolean existsByExternalId(String externalId) {
        return tenantExistsOutput.existsByExternalId(externalId);
    }

    @Override
    @Observed(name = "DOMAIN_TENANT_EXISTS_INTERNAL_ID")
    public boolean existsById(CompositeId compositeId) {
        return tenantExistsOutput.existsById(compositeId);
    }
}
