package com.acme.jga.domain.output.functions.users;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.generic.CompositeId;

public interface UserDeleteOutput {
    Integer deleteForOrganization(CompositeId tenantId, CompositeId organizationId, CompositeId id, EventData eventData);

    Integer deleteForOrganization(CompositeId tenantId, CompositeId organizationId);

    Integer deleteForTenant(CompositeId tenantId);
}
