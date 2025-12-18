package com.acme.jga.spi.adapter.sector;

import com.acme.jga.domain.output.functions.sectors.SectorFindOuput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.spi.dao.sectors.api.SectorsDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorFindOutputImpl implements SectorFindOuput {

    private final SectorsDao sectorsDao;

    public SectorFindOutputImpl(SectorsDao sectorsDao) {
        this.sectorsDao = sectorsDao;
    }

    @Override
    public List<Sector> findAll(CompositeId tenantId, CompositeId organizationId) {
        return sectorsDao.findAll(tenantId, organizationId);
    }

    @Override
    public Sector findById(CompositeId tenantId, CompositeId organizationId, CompositeId sectorId) {
        return sectorsDao.findById(tenantId, organizationId, sectorId);
    }
}
