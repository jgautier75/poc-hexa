package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.events.EventUpdateOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationUpdateOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationUpdateOutputImpl implements OrganizationUpdateOutput {
    private final OrganizationsDao organizationsDao;
    private final EventUpdateOutput eventUpdateOutput;


    public OrganizationUpdateOutputImpl(OrganizationsDao organizationsDao, EventUpdateOutput eventUpdateOutput) {
        this.organizationsDao = organizationsDao;
        this.eventUpdateOutput = eventUpdateOutput;
    }

    @Override
    public Integer update(Organization organization, EventData eventData) {
        Integer nbUpdated = this.organizationsDao.update(organization.tenantId(), organization.id(),
                organization.code(), organization.label(), organization.country(), organization.status());
        eventUpdateOutput.saveChanges(eventData);
        return nbUpdated;
    }
}
