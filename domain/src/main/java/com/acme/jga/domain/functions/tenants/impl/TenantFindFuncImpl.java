package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.functions.tenants.api.TenantExistsFunc;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantId;
import com.acme.jga.domain.output.functions.tenants.TenantFindOutput;

@DomainService
public class TenantFindFuncImpl implements TenantFindInput {
    private final TenantFindOutput tenantFindOutput;
    private final TenantExistsFunc tenantExistsFunc;

    public TenantFindFuncImpl(TenantFindOutput tenantFindOutput, TenantExistsFunc tenantExistsFunc) {
        this.tenantFindOutput = tenantFindOutput;
        this.tenantExistsFunc = tenantExistsFunc;
    }

    @Override
    public Tenant findByCode(String code) throws FunctionalException {

        // Ensure tenant exists
        boolean exists = tenantExistsFunc.existByCode(code);
        if (!exists) {
            throwException(FunctionalErrors.NOT_FOUND.name(), "tenant.not_found", code);
        }

        // Fetch tenant
        return tenantFindOutput.findByCode(code);
    }

    @Override
    public Tenant findById(TenantId tenantId) throws FunctionalException {

        // Ensure tenant exists
        boolean exists = tenantExistsFunc.existsByExternalId(tenantId.get());
        if (!exists) {
            throwException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), "tenant.not_found", tenantId.get());
        }

        return null;
    }

    private void throwException(String scope, String code, String key, Object... args) throws FunctionalException {
        throw new FunctionalException(scope, key, BundleFactory.getMessage(code, args));
    }

}
