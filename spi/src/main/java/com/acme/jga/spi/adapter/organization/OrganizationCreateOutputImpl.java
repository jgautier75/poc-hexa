package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.output.functions.organizations.OrganizationCreateOutput;
import com.acme.jga.domain.output.functions.sectors.SectorCreateOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class OrganizationCreateOutputImpl implements OrganizationCreateOutput {
    private final OrganizationsDao organizationsDao;
    private final SectorCreateOutput sectorCreateOutput;

    public OrganizationCreateOutputImpl(OrganizationsDao organizationsDao, SectorCreateOutput sectorCreateOutput) {
        this.organizationsDao = organizationsDao;
        this.sectorCreateOutput = sectorCreateOutput;
    }

    @Override
    public CompositeId save(Organization organization) {
        return this.organizationsDao.save(organization);
    }

    @Override
    public CompositeId save(Organization organization, Sector rootSector) {
        CompositeId orgId = this.organizationsDao.save(organization);
        Sector s = new Sector(null, organization.tenantId(), orgId, organization.label(), organization.code(), null, true, Collections.emptyList());
        sectorCreateOutput.create(s);
        return orgId;
    }

}
