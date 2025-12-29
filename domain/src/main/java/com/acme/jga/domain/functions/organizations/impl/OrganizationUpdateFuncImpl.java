package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationUpdateInput;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationUpdateOutput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;

@DomainService
public class OrganizationUpdateFuncImpl implements OrganizationUpdateInput {

    private final TenantExistsOutput tenantExistsOutput;
    private final OrganizationFindOutput organizationFindOutput;
    private final OrganizationUpdateOutput organizationUpdateOutput;

    public OrganizationUpdateFuncImpl(TenantExistsOutput tenantExistsOutput, OrganizationFindOutput organizationFindOutput, OrganizationUpdateOutput organizationUpdateOutput) {
        this.tenantExistsOutput = tenantExistsOutput;
        this.organizationFindOutput = organizationFindOutput;
        this.organizationUpdateOutput = organizationUpdateOutput;
    }


    @Override
    public void update(Organization organization) throws FunctionalException {
        boolean tenantExists = this.tenantExistsOutput.existsByExternalId(organization.tenantId().externalId(), null);
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", organization.tenantId().externalId()));
        }
        Organization orgByCode = this.organizationFindOutput.findByCode(organization.code());
        if (orgByCode != null && orgByCode.code().equals(organization.code())) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.ALREADY_EXISTS.name(), BundleFactory.getMessage("organization.already_exist", organization.code()));
        }
        this.organizationUpdateOutput.update(organization);
    }
}
