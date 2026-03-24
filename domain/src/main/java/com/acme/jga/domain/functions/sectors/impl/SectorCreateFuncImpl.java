package com.acme.jga.domain.functions.sectors.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.ports.input.organizations.OrganizationFindInput;
import com.acme.jga.domain.ports.input.sectors.SectorFindInput;
import com.acme.jga.domain.ports.input.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.ports.input.sectors.SectorCreateInput;
import com.acme.jga.domain.ports.output.sectors.SectorCreateOutput;

@DomainService
public class SectorCreateFuncImpl implements SectorCreateInput {
    private final SectorCreateOutput sectorCreateOutput;
    private final TenantFindInput tenantFindInput;
    private final OrganizationFindInput organizationFindInput;
    private final SectorFindInput sectorFindInput;

    public SectorCreateFuncImpl(SectorCreateOutput sectorCreateOutput, TenantFindInput tenantFindInput, OrganizationFindInput organizationFindInput, SectorFindInput sectorFindInput) {
        this.sectorCreateOutput = sectorCreateOutput;
        this.tenantFindInput = tenantFindInput;
        this.organizationFindInput = organizationFindInput;
        this.sectorFindInput = sectorFindInput;
    }

    @Override
    public CompositeId create(Sector sector) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(sector.getTenantId());
        Organization organization = organizationFindInput.findById(tenant.id(), sector.getOrganizationId());
        Sector parentSector = sectorFindInput.findById(tenant.id(), organization.id(), sector.getParent());
        if (parentSector == null) {
            throw new FunctionalException(Scope.SECTOR.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("sector_not_found", sector.getId().get()));
        }
        Sector s = new Sector(null, tenant.id(), organization.id(), sector.getLabel(), sector.getCode(), parentSector.getId(), false, null);
        return sectorCreateOutput.create(s);
    }
}
