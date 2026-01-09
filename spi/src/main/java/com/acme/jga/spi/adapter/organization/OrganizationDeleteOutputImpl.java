package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.events.EventOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationDeleteOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationDeleteOutputImpl implements OrganizationDeleteOutput {
    private final OrganizationsDao organizationsDao;
    private final EventOutput eventOutput;

    public OrganizationDeleteOutputImpl(OrganizationsDao organizationsDao,
                                        EventOutput eventOutput) {
        this.organizationsDao = organizationsDao;
        this.eventOutput = eventOutput;
    }

    @Override
    public Integer delete(CompositeId tenantId, CompositeId orgId, EventData eventData) {
        eventOutput.saveChanges(eventData);
        return this.organizationsDao.delete(tenantId, orgId);
    }
}
