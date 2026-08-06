package com.acme.jga.domain.ports.secondary.sectors;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;

public interface SectorCreateOutput {
    CompositeId create(Sector sector);
}
