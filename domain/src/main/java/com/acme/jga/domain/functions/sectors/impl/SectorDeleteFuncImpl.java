package com.acme.jga.domain.functions.sectors.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.sectors.SectorFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.output.functions.sectors.SectorDeleteOutput;
import com.acme.jga.domain.output.functions.sectors.SectorFindOuput;

@DomainService
public class SectorDeleteFuncImpl implements SectorDeleteOutput {
    private final TenantFindInput tenantFindInput;
    private final OrganizationFindInput organizationFindInput;
    private final SectorFindInput sectorFindInput;
    private final SectorFindOuput sectorFindOuput;

    public SectorDeleteFuncImpl(TenantFindInput tenantFindInput, OrganizationFindInput organizationFindInput, SectorFindInput sectorFindInput, SectorFindOuput sectorFindOuput) {
        this.tenantFindInput = tenantFindInput;
        this.organizationFindInput = organizationFindInput;
        this.sectorFindInput = sectorFindInput;
        this.sectorFindOuput = sectorFindOuput;
    }

    @Override
    public Integer delete(Sector sector) {
        return 0;
    }
}
