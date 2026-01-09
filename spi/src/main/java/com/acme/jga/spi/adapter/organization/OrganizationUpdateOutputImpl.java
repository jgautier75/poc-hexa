package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.events.EventOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationUpdateOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationUpdateOutputImpl implements OrganizationUpdateOutput {
    private final OrganizationsDao organizationsDao;
    private final EventOutput eventOutput;

    public OrganizationUpdateOutputImpl(OrganizationsDao organizationsDao, EventOutput eventOutput) {
        this.organizationsDao = organizationsDao;
        this.eventOutput = eventOutput;
    }

    @Override
    public Integer update(Organization organization, EventData eventData) {
        Integer nbUpdated = this.organizationsDao.update(organization.tenantId(), organization.id(),
                organization.code(), organization.label(), organization.country(), organization.status());
        eventOutput.saveChanges(eventData);
        return nbUpdated;
    }
}
