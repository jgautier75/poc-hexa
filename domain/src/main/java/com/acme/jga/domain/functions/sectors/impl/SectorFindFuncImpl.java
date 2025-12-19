package com.acme.jga.domain.functions.sectors.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.input.functions.sectors.SectorFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.output.functions.sectors.SectorFindOuput;

import java.util.List;

@DomainService()
public class SectorFindFuncImpl implements SectorFindInput {
    private final SectorFindOuput sectorFindOuput;

    public SectorFindFuncImpl(SectorFindOuput sectorFindOuput) {
        this.sectorFindOuput = sectorFindOuput;
    }

    @Override
    public List<Sector> findAll(CompositeId tenantId, CompositeId organizationId) {
        return sectorFindOuput.findAll(tenantId, organizationId);
    }

    @Override
    public Sector findById(CompositeId tenantId, CompositeId organizationId, CompositeId sectorId) {
        return sectorFindOuput.findById(tenantId, organizationId, sectorId);
    }
}
