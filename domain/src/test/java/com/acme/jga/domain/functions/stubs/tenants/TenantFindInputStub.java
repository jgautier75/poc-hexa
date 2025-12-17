package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;

public class TenantFindInputStub implements TenantFindInput {

    private final Tenant tenant;

    public TenantFindInputStub(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public Tenant findByCode(String code) throws FunctionalException {
        return tenant.code().equals(code) ? tenant : null;
    }

    @Override
    public Tenant findById(CompositeId tenantId) throws FunctionalException {
        return tenant.id().internalId().longValue() == tenantId.internalId().longValue() ? tenant : null;
    }
}
