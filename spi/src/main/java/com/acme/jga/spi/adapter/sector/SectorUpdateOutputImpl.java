package com.acme.jga.spi.adapter.sector;

import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.output.functions.sectors.SectorUpdateOutput;
import com.acme.jga.spi.dao.sectors.api.SectorsDao;
import org.springframework.stereotype.Service;

@Service
public class SectorUpdateOutputImpl implements SectorUpdateOutput {

    private final SectorsDao sectorsDao;

    public SectorUpdateOutputImpl(SectorsDao sectorsDao) {
        this.sectorsDao = sectorsDao;
    }

    @Override
    public Integer update(Sector sector) {
        return this.sectorsDao.update(sector);
    }
}
