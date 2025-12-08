package com.acme.jga.domain.impl.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.impl.functions.tenants.api.TenantExistsFunc;
import com.acme.jga.domain.input.functions.tenants.TenantCreateInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantId;
import com.acme.jga.domain.output.functions.tenants.TenantCreateOutput;

@DomainService
public class TenantCreateFuncImpl implements TenantCreateInput {
    private final TenantExistsFunc tenantExistsFunc;
    private final TenantCreateOutput tenantCreateOutput;

    public TenantCreateFuncImpl(TenantExistsFunc tenantExistsFunc,
                                TenantCreateOutput tenantCreateOutput) {
        this.tenantExistsFunc = tenantExistsFunc;
        this.tenantCreateOutput = tenantCreateOutput;
    }

    @Override
    public TenantId create(Tenant tenant) throws FunctionalException {
        boolean alreadyExists = tenantExistsFunc.existByCode(tenant.code());
        if (alreadyExists) {
            throw new FunctionalException(BundleFactory.getMessage("tenant.already_exist", tenant.code()), FunctionalErrors.NOT_FOUND.name());
        }
        CompositeId compositeId = tenantCreateOutput.save(tenant);
        return compositeId::externalId;
    }

}
