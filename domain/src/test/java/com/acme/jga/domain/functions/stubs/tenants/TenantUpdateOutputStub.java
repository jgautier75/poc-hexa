package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantUpdateOutput;

import java.util.List;

public class TenantUpdateOutputStub implements TenantUpdateOutput {

    private final List<Tenant> tenants;

    public TenantUpdateOutputStub(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    @Override
    public boolean update(Tenant tenant) throws FunctionalException {
        return tenants.stream().anyMatch(t -> t.id().get().equals(tenant.id().get()));
    }
}
