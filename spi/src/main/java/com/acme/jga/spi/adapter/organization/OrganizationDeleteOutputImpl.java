package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.events.EventOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationDeleteOutput;
import com.acme.jga.domain.output.functions.sectors.SectorDeleteOutput;
import com.acme.jga.domain.output.functions.users.UserDeleteOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationDeleteOutputImpl implements OrganizationDeleteOutput {
    private final OrganizationsDao organizationsDao;
    private final UserDeleteOutput userDeleteOutput;
    private final EventOutput eventOutput;
    private final SectorDeleteOutput sectorDeleteOutput;

    public OrganizationDeleteOutputImpl(OrganizationsDao organizationsDao,
                                        UserDeleteOutput userDeleteOutput,
                                        EventOutput eventOutput,
                                        SectorDeleteOutput sectorDeleteOutput) {
        this.organizationsDao = organizationsDao;
        this.userDeleteOutput = userDeleteOutput;
        this.eventOutput = eventOutput;
        this.sectorDeleteOutput = sectorDeleteOutput;
    }

    @Override
    public Integer delete(CompositeId tenantId, CompositeId orgId, EventData eventData) {
        eventOutput.saveChanges(eventData);
        userDeleteOutput.deleteForOrganization(tenantId, orgId);
        sectorDeleteOutput.deleteForOrg(tenantId, orgId);
        return this.organizationsDao.delete(tenantId, orgId);
    }

    @Override
    public Integer deleteByTenant(CompositeId tenantId) {
        userDeleteOutput.deleteForTenant(tenantId);
        sectorDeleteOutput.deleteForTenant(tenantId);
        return organizationsDao.deleteByTenant(tenantId);
    }
}
