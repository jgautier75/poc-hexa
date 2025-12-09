package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantListOutput;

import java.util.List;

public class TenantListOutputStub implements TenantListOutput {
    private final List<Tenant> tenants;

    public TenantListOutputStub(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    @Override
    public List<Tenant> list() {
        return tenants;
    }
}
