package com.acme.jga.domain.impl.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.impl.functions.tenants.api.TenantExistsFunc;
import com.acme.jga.domain.input.functions.tenants.TenantDeleteInput;
import com.acme.jga.domain.model.generic.CompositeId;
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
    public boolean deleteTenant(String uid) throws FunctionalException {
        boolean exists = this.tenantExistsFunc.existsByExternalId(uid);
        if (!exists) {
            throw new FunctionalException(FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", uid));
        }
        CompositeId cid = new CompositeId(IdKind.STRING_ONLY, null, uid);
        return this.tenantDeleteOuput.deleteTenant(cid);
    }
}
