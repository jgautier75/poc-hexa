package com.acme.jga.domain.impl.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.impl.functions.tenants.api.TenantCreate;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantId;
import com.acme.jga.domain.output.functions.tenants.TenantExists;

@DomainService
public class TenantCreateImpl implements TenantCreate {
    private final TenantExists tenantExists;
    private final com.acme.jga.domain.output.functions.tenants.TenantCreate tenantCreate;

    public TenantCreateImpl(TenantExists tenantExists,
                            com.acme.jga.domain.output.functions.tenants.TenantCreate tenantCreate) {
        this.tenantExists = tenantExists;
        this.tenantCreate = tenantCreate;
    }

    @Override
    public TenantId create(Tenant tenant) throws FunctionalException {
        boolean alreadyExists = tenantExists.exists(tenant.code());
        if (alreadyExists) {
            throw new FunctionalException(BundleFactory.getMessage("tenant.already_exist", tenant.code()), FunctionalErrors.NOT_FOUND.name());
        }
        CompositeId compositeId = tenantCreate.save(tenant);
        return compositeId::externalId;
    }

}
