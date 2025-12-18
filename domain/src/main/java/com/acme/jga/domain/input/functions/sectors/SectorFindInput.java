package com.acme.jga.domain.input.functions.sectors;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;

import java.util.List;

public interface SectorFindInput {
    List<Sector> findAll(CompositeId tenantId, CompositeId organizationId);
    Sector findById(CompositeId tenantId, CompositeId organizationId,CompositeId sectorId);
}
