package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.tenants.api.TenantExistsFunc;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;

import java.util.List;

@DomainService
public class OrganizationFindFuncImpl implements OrganizationFindInput {

    private final TenantExistsFunc tenantExistsFunc;
    private final OrganizationFindOutput organizationFindOutput;

    public OrganizationFindFuncImpl(TenantExistsFunc tenantExistsFunc, OrganizationFindOutput organizationFindOutput) {
        this.tenantExistsFunc = tenantExistsFunc;
        this.organizationFindOutput = organizationFindOutput;
    }


    @Override
    public List<Organization> findAll(CompositeId tenantId) throws FunctionalException {
        boolean tenantExists = tenantExistsFunc.existsByExternalId(tenantId.externalId());
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", tenantId.externalId()));
        }
        return organizationFindOutput.findAll(tenantId);
    }

    @Override
    public Organization findById(CompositeId tenantId, CompositeId organizationId) throws FunctionalException {
        boolean tenantExists = tenantExistsFunc.existsByExternalId(tenantId.externalId());
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", tenantId.externalId()));
        }
        Organization org = this.organizationFindOutput.findById(tenantId, organizationId);
        if (org == null) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("organization.not_found", organizationId.externalId()));
        }
        return org;
    }
}
