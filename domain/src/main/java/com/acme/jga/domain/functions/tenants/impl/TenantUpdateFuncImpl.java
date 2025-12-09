package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.input.functions.tenants.TenantUpdateInput;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantUpdateOutput;

@DomainService
public class TenantUpdateFuncImpl implements TenantUpdateInput {

    private final TenantUpdateOutput tenantUpdateOutput;

    public TenantUpdateFuncImpl(TenantUpdateOutput tenantUpdateOutput) {
        this.tenantUpdateOutput = tenantUpdateOutput;
    }

    @Override
    public boolean update(Tenant tenant) {
        return tenantUpdateOutput.update(tenant);
    }
}
