package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.tenants.TenantDeleteInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.ExternalId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationDeleteOutput;
import com.acme.jga.domain.output.functions.tenants.TenantDeleteOuput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;

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
