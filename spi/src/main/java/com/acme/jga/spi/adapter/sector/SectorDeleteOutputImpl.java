package com.acme.jga.spi.adapter.sector;

import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.output.functions.sectors.SectorDeleteOutput;
import com.acme.jga.spi.dao.sectors.api.SectorsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectorDeleteOutputImpl implements SectorDeleteOutput {

    private final SectorsDao sectorsDao;

    public SectorDeleteOutputImpl(SectorsDao sectorsDao) {
        this.sectorsDao = sectorsDao;
    }

    @Override
    public Integer delete(Sector sector) {
        return this.sectorsDao.delete(sector.getTenantId(), sector.getOrganizationId(), sector.getId(), sector.getParent());
    }
}
