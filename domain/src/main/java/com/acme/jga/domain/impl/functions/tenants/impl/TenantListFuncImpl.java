package com.acme.jga.domain.impl.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.input.functions.tenants.TenantListInput;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantListOutput;

import java.util.List;

@DomainService
public class TenantListFuncImpl implements TenantListInput {
    private final TenantListOutput tenantListOutput;

    public TenantListFuncImpl(TenantListOutput tenantListOutput) {
        this.tenantListOutput = tenantListOutput;
    }

    @Override
    public List<Tenant> list() {
        return tenantListOutput.list();
    }
}
