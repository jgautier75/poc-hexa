package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.tenants.api.TenantExistsFunc;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationCreateInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.organizations.OrganizationCreateOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;

@DomainService
public class OrganizationCreateFuncImpl implements OrganizationCreateInput {
    private final TenantExistsFunc tenantExistsFunc;
    private final OrganizationFindOutput organizationFindOutput;
    private final OrganizationCreateOutput organizationCreateOutput;

    public OrganizationCreateFuncImpl(TenantExistsFunc tenantExistsFunc, OrganizationFindOutput organizationFindOutput, OrganizationCreateOutput organizationCreateOutput) {
        this.tenantExistsFunc = tenantExistsFunc;
        this.organizationFindOutput = organizationFindOutput;
        this.organizationCreateOutput = organizationCreateOutput;
    }


    @Override
    public CompositeId create(Organization organization) throws FunctionalException {

        boolean tenantExists = tenantExistsFunc.existsByExternalId(organization.tenantId().externalId());
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", organization.tenantId().externalId()));
        }

        boolean orgCodeExists = organizationFindOutput.existsByCode(organization.code());
        if (!orgCodeExists) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.ALREADY_EXISTS.name(), BundleFactory.getMessage("organization.already_exist", organization.code()));
        }

        return organizationCreateOutput.save(organization);
    }
}
