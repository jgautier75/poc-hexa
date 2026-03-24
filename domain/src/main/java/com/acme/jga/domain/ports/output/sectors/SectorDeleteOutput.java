package com.acme.jga.domain.ports.output.sectors;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;

public interface SectorDeleteOutput {
    Integer delete(Sector sector) throws FunctionalException;
    Integer deleteForOrg(CompositeId tenantId, CompositeId organizationId);
    Integer deleteForTenant(CompositeId tenantId);
}
