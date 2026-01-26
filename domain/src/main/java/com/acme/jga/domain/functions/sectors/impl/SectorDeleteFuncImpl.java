package com.acme.jga.domain.functions.sectors.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.sectors.SectorDeleteInput;
import com.acme.jga.domain.input.functions.sectors.SectorFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.sectors.SectorDeleteOutput;

@DomainService
public class SectorDeleteFuncImpl implements SectorDeleteInput {
    private final TenantFindInput tenantFindInput;
    private final OrganizationFindInput organizationFindInput;
    private final SectorFindInput sectorFindInput;
    private final SectorDeleteOutput sectorDeleteOutput;

    public SectorDeleteFuncImpl(TenantFindInput tenantFindInput,
                                OrganizationFindInput organizationFindInput,
                                SectorFindInput sectorFindInput,
                                SectorDeleteOutput sectorDeleteOutput) {
        this.tenantFindInput = tenantFindInput;
        this.organizationFindInput = organizationFindInput;
        this.sectorFindInput = sectorFindInput;
        this.sectorDeleteOutput = sectorDeleteOutput;
    }


    @Override
    public Integer deleteSector(CompositeId tenantId, CompositeId organizationId, CompositeId sectorId) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(sectorId);
        Organization organization = organizationFindInput.findById(tenant.id(), organizationId);
        Sector rdbmsSector = sectorFindInput.findById(tenant.id(), organization.id(), sectorId);
        return sectorDeleteOutput.delete(rdbmsSector);
    }

    @Override
    public Integer deleteAll(CompositeId tenantId, CompositeId organizationId) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(tenantId);
        Organization organization = organizationFindInput.findById(tenant.id(), organizationId);
        return sectorDeleteOutput.deleteForOrg(tenant.id(), organization.id());
    }
}
