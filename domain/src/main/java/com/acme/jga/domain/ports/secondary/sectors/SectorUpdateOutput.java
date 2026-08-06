package com.acme.jga.domain.ports.secondary.sectors;

import com.acme.jga.domain.model.sector.Sector;

public interface SectorUpdateOutput {

    Integer update(Sector sector);
}
