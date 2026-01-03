package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
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
    public boolean existsByExternalId(String externalId) throws FunctionalException {
        return tenantExistsOutput.existsByExternalId(externalId);
    }

    @Override
    public boolean existsById(CompositeId compositeId) throws FunctionalException {
        return tenantExistsOutput.existsById(compositeId);
    }
}
