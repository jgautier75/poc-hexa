package com.acme.jga.spi.dao.sectors.api;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;

import java.util.List;

public interface SectorsDao {
    CompositeId save(Sector sector);

    Integer update(Sector sector);

    Sector findById(CompositeId tenantId, CompositeId organizationId, CompositeId sectorId);

    List<Sector> findAll(CompositeId tenantId, CompositeId organizationId);

    Integer delete(CompositeId tenantId, CompositeId organizationId, CompositeId sectorId, CompositeId sectorParentId);

    Integer deleteForOrganization(CompositeId tenantId, CompositeId organizationId);

    Integer deleteForTenant(CompositeId tenantId);
}
