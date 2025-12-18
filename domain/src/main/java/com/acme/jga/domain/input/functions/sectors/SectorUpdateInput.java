package com.acme.jga.domain.input.functions.sectors;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.sector.Sector;

public interface SectorUpdateInput {
    void update(Sector sector) throws FunctionalException;
}
