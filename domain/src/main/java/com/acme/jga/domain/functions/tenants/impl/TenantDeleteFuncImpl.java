package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.ports.input.tenants.TenantDeleteInput;
import com.acme.jga.domain.ports.input.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.ports.output.tenants.TenantDeleteOuput;

@DomainService
public class TenantDeleteFuncImpl implements TenantDeleteInput {
    private final TenantFindInput tenantFindInput;
    private final TenantDeleteOuput tenantDeleteOuput;

    public TenantDeleteFuncImpl(TenantFindInput tenantFindInput,
                                TenantDeleteOuput tenantDeleteOuput) {
        this.tenantFindInput = tenantFindInput;
        this.tenantDeleteOuput = tenantDeleteOuput;
    }

    @Override
    public boolean deleteTenant(CompositeId id) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(id);
        return this.tenantDeleteOuput.deleteTenant(tenant.id());
    }
}
