package com.acme.jga.spi.adapter.sector;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.output.functions.sectors.SectorCreateOutput;
import com.acme.jga.spi.dao.sectors.api.SectorsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectorCreateOutputImpl implements SectorCreateOutput {

    private final SectorsDao sectorsDao;

    public SectorCreateOutputImpl(SectorsDao sectorsDao) {
        this.sectorsDao = sectorsDao;
    }

    @Override
    public CompositeId create(Sector sector) {
        return this.sectorsDao.save(sector);
    }
}
