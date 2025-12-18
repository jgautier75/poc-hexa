package com.acme.jga.domain.functions.sectors.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.sectors.SectorUpdateInput;
import com.acme.jga.domain.model.sector.Sector;

@DomainService
public class SectorUpdateFuncImpl implements SectorUpdateInput {


    @Override
    public void update(Sector sector) throws FunctionalException {

    }
}
