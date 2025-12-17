package com.acme.jga.domain.functions.sectors.api;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;

import java.util.List;


public interface SectorFindOuput {
    List<Sector> findAll(CompositeId tenantId, CompositeId organizationId);
}
