package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.output.functions.events.EventOutput;
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
    private final EventPublisher eventPublisher;
    private final EventOutput eventOutput;

    public OrganizationCreateOutputImpl(OrganizationsDao organizationsDao,
                                        SectorCreateOutput sectorCreateOutput,
                                        EventPublisher eventPublisher,
                                        EventOutput eventOutput) {
        this.organizationsDao = organizationsDao;
        this.sectorCreateOutput = sectorCreateOutput;
        this.eventPublisher = eventPublisher;
        this.eventOutput = eventOutput;
    }

    @Override
    public CompositeId save(Organization organization) {
        return this.organizationsDao.save(organization);
    }

    @Override
    public CompositeId save(Organization organization, Sector rootSector, EventData eventData) {
        CompositeId orgId = this.organizationsDao.save(organization);
        Sector s = new Sector(null, organization.tenantId(), orgId, rootSector.getLabel(), rootSector.getCode(), null, true, Collections.emptyList());
        sectorCreateOutput.create(s);
        EventData updatedEventData = new EventData(eventData.contextUser(), orgId.externalId(), eventData.scope(), eventData.auditAction(), eventData.target(), eventData.auditChanges());
        eventOutput.saveChanges(updatedEventData);
        return orgId;
    }

}
