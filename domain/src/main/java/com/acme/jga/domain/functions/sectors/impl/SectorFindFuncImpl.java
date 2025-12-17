package com.acme.jga.domain.functions.sectors.impl;

import com.acme.jga.domain.functions.sectors.api.SectorFindOuput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;

import java.util.List;

public class SectorFindFuncImpl implements SectorFindOuput {

    private final SectorFindOuput sectorFindOuput;

    public SectorFindFuncImpl(SectorFindOuput sectorFindOuput) {
        this.sectorFindOuput = sectorFindOuput;
    }

    @Override
    public List<Sector> findAll(CompositeId tenantId, CompositeId organizationId) {
        return sectorFindOuput.findAll(tenantId, organizationId);
    }
}
