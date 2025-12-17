package com.acme.jga.domain.functions.sectors.api;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;

public interface SectorsListInput {

    Sector findSectorHierarchy(CompositeId tenantId, CompositeId organizationId) throws FunctionalException;

}
