package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationCreateInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationCreateOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.output.functions.sectors.SectorCreateOutput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class OrganizationCreateFuncImpl implements OrganizationCreateInput {
    private final TenantExistsInput tenantExistsFunc;
    private final TenantFindInput tenantFindInput;
    private final SectorCreateOutput sectorCreateOutput;
    private final OrganizationFindOutput organizationFindOutput;
    private final OrganizationCreateOutput organizationCreateOutput;

    public OrganizationCreateFuncImpl(TenantExistsInput tenantExistsFunc, TenantFindInput tenantFindInput, SectorCreateOutput sectorCreateOutput, OrganizationFindOutput organizationFindOutput, OrganizationCreateOutput organizationCreateOutput) {
        this.tenantExistsFunc = tenantExistsFunc;
        this.tenantFindInput = tenantFindInput;
        this.sectorCreateOutput = sectorCreateOutput;
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
        if (orgCodeExists) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.ALREADY_EXISTS.name(), BundleFactory.getMessage("organization.already_exist", organization.code()));
        }

        Tenant tenant = tenantFindInput.findById(organization.tenantId());

        Organization org = new Organization(null, tenant.id(), organization.label(), organization.code(), organization.kind(), organization.country(), organization.status());
        CompositeId orgCompositeId = organizationCreateOutput.save(org);

        // Always create a root sector when creating an organization
        Sector sector = new Sector(null, tenant.id(), orgCompositeId, organization.label(), organization.code(), null, true, null);
        this.sectorCreateOutput.create(sector);

        return orgCompositeId;
    }
}
