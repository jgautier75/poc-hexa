package com.acme.jga.domain.ports.output.organizations;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.generic.CompositeId;

public interface OrganizationDeleteOutput {
    Integer delete(CompositeId tenantId, CompositeId orgId, EventData eventData);
    Integer deleteByTenant(CompositeId tenantId);
}
