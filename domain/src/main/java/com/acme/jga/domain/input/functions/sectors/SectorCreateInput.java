package com.acme.jga.domain.input.functions.sectors;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;

public interface SectorCreateInput {
    CompositeId create(Sector sector) throws FunctionalException;
}
