package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.tenants.TenantUpdateInput;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantUpdateOutput;

@DomainService
public class TenantUpdateFuncImpl implements TenantUpdateInput {
    private final TenantUpdateOutput tenantUpdateOutput;
    private final TenantExistsFuncImpl tenantExistsFunc;

    public TenantUpdateFuncImpl(TenantUpdateOutput tenantUpdateOutput, TenantExistsFuncImpl tenantExistsFunc) {
        this.tenantUpdateOutput = tenantUpdateOutput;
        this.tenantExistsFunc = tenantExistsFunc;
    }

    @Override
    public boolean update(Tenant tenant) throws FunctionalException {
        boolean exists = tenantExistsFunc.existsByExternalId(tenant.id().get());
        if (!exists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", tenant.id().get()));
        }
        return tenantUpdateOutput.update(tenant);
    }
}
