package com.acme.jga.domain.input.functions.sectors;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;

public interface SectorDeleteInput {
    Integer deleteSector(CompositeId tenantId, CompositeId organizationId, CompositeId sectorId) throws FunctionalException;

    Integer deleteAll(CompositeId tenantId, CompositeId organizationId) throws FunctionalException;

}
