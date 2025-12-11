package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;

import java.util.List;

public class TenantExistsOutputStub implements TenantExistsOutput {

    private final List<Tenant> tenants;

    public TenantExistsOutputStub(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    @Override
    public boolean existsByCode(String code) {
        return tenants.stream().anyMatch(t -> t.code().equals(code));
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        return tenants.stream().anyMatch(t -> t.id().get().equals(externalId));
    }
}
