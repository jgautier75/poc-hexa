package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.organizations.validation.OrganizationUpdateValidationHolder;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationUpdateInput;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationUpdateOutput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;
import com.acme.jga.domain.output.functions.tenants.TenantFindOutput;

@DomainService
public class OrganizationUpdateFuncImpl implements OrganizationUpdateInput {
    private final TenantFindOutput tenantFindOutput;
    private final TenantExistsOutput tenantExistsOutput;
    private final OrganizationFindOutput organizationFindOutput;
    private final OrganizationUpdateOutput organizationUpdateOutput;

    public OrganizationUpdateFuncImpl(TenantFindOutput tenantFindOutput, TenantExistsOutput tenantExistsOutput, OrganizationFindOutput organizationFindOutput, OrganizationUpdateOutput organizationUpdateOutput) {
        this.tenantFindOutput = tenantFindOutput;
        this.tenantExistsOutput = tenantExistsOutput;
        this.organizationFindOutput = organizationFindOutput;
        this.organizationUpdateOutput = organizationUpdateOutput;
    }


    @Override
    public void update(Organization organization) throws FunctionalException {

        // Validate
        OrganizationUpdateValidationHolder.getInstance().validate(organization);

        boolean tenantExists = this.tenantExistsOutput.existsByExternalId(organization.tenantId().externalId());
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", organization.tenantId().externalId()));
        }

        Tenant tenant = tenantFindOutput.findById(organization.tenantId());

        // Ensure organization code is not already used
        Organization rdbmsOrg = this.organizationFindOutput.findById(tenant.id(), organization.id());
        Organization orgByCode = this.organizationFindOutput.findByCode(organization.code());
        if (orgByCode != null && orgByCode.id().internalId().longValue() != rdbmsOrg.id().internalId().longValue()) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.ALREADY_EXISTS.name(), BundleFactory.getMessage("organization.code_already_used", organization.code()));
        }

        Organization updateOrg = new Organization(rdbmsOrg.id(), tenant.id(), organization.label(), organization.code(), organization.kind(), organization.country(), organization.status());
        this.organizationUpdateOutput.update(updateOrg);
    }
}
