package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.tenants.api.TenantExistsFunc;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.tenants.TenantDeleteInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.ExternalId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.domain.output.functions.tenants.TenantDeleteOuput;

@DomainService
public class TenantDeleteFuncImpl implements TenantDeleteInput {
    private final TenantExistsFunc tenantExistsFunc;
    private final TenantDeleteOuput tenantDeleteOuput;

    public TenantDeleteFuncImpl(TenantExistsFunc tenantExistsFunc, TenantDeleteOuput tenantDeleteOuput) {
        this.tenantExistsFunc = tenantExistsFunc;
        this.tenantDeleteOuput = tenantDeleteOuput;
    }

    @Override
    public boolean deleteTenant(ExternalId id) throws FunctionalException {
        boolean exists = this.tenantExistsFunc.existsByExternalId(id.get());
        if (!exists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", id.get()));
        }
        CompositeId cid = new CompositeId(null, id.get());
        return this.tenantDeleteOuput.deleteTenant(cid);
    }
}
