package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.functions.tenants.api.TenantExistsFunc;
import com.acme.jga.domain.model.tenant.Tenant;

import java.util.List;

public class TenantExistsFuncStub implements TenantExistsFunc {

    private final List<Tenant> tenants;

    public TenantExistsFuncStub(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    @Override
    public boolean existsByCode(String code) {
        return this.tenants.stream().anyMatch(t -> t.code().equals(code));
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        return this.tenants.stream().anyMatch(t -> t.id().externalId().equals(externalId));
    }
}
