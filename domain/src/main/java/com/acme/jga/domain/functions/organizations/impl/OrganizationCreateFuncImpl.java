package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.organizations.validation.OrganizationCreateValidationHolder;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationCreateInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationCreateOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;

@DomainService
public class OrganizationCreateFuncImpl implements OrganizationCreateInput {
    private final TenantExistsInput tenantExistsFunc;
    private final TenantFindInput tenantFindInput;
    private final OrganizationFindOutput organizationFindOutput;
    private final OrganizationCreateOutput organizationCreateOutput;

    public OrganizationCreateFuncImpl(TenantExistsInput tenantExistsFunc, TenantFindInput tenantFindInput, OrganizationFindOutput organizationFindOutput, OrganizationCreateOutput organizationCreateOutput) {
        this.tenantExistsFunc = tenantExistsFunc;
        this.tenantFindInput = tenantFindInput;
        this.organizationFindOutput = organizationFindOutput;
        this.organizationCreateOutput = organizationCreateOutput;
    }

    @Override
    public CompositeId create(Organization organization) throws FunctionalException {

        // Ensure tenant exists
        boolean tenantExists = tenantExistsFunc.existsByExternalId(organization.tenantId().externalId());
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", organization.tenantId().externalId()));
        }

        // Ensure organization code is not already used
        boolean orgCodeExists = organizationFindOutput.existsByCode(organization.code());
        if (orgCodeExists) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.ALREADY_EXISTS.name(), BundleFactory.getMessage("organization.code_already_used", organization.code()));
        }

        // Validate payload
        OrganizationCreateValidationHolder.getInstance().validate(organization);

        Tenant tenant = tenantFindInput.findById(organization.tenantId());
        Organization org = new Organization(null, tenant.id(), organization.label(), organization.code(), organization.kind(), organization.country(), organization.status());
        return organizationCreateOutput.save(org);
    }
}
