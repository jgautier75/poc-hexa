package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;

import java.util.List;

@DomainService
public class OrganizationFindFuncImpl implements OrganizationFindInput {
    private final TenantExistsInput tenantExistsFunc;
    private final OrganizationFindOutput organizationFindOutput;
    private final TenantFindInput tenantFindInput;

    public OrganizationFindFuncImpl(TenantExistsInput tenantExistsFunc, OrganizationFindOutput organizationFindOutput, TenantFindInput tenantFindInput) {
        this.tenantExistsFunc = tenantExistsFunc;
        this.organizationFindOutput = organizationFindOutput;
        this.tenantFindInput = tenantFindInput;
    }

    @Override
    public List<Organization> findAll(CompositeId tenantId) throws FunctionalException {
        boolean tenantExists = tenantExistsFunc.existsByExternalId(tenantId.externalId());
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", tenantId.externalId()));
        }
        Tenant tenant = tenantFindInput.findById(tenantId);
        return organizationFindOutput.findAll(tenant.id());
    }

    @Override
    public Organization findById(CompositeId tenantId, CompositeId organizationId) throws FunctionalException {
        boolean tenantExists = tenantExistsFunc.existsByExternalId(tenantId.externalId());
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", tenantId.externalId()));
        }

        Tenant tenant = tenantFindInput.findById(tenantId);
        Organization org = this.organizationFindOutput.findById(tenant.id(), organizationId);
        if (org == null) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("organization.not_found", organizationId.externalId()));
        }
        return org;
    }
}
