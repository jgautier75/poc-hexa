package com.acme.jga.domain.output.functions.organizations;

import com.acme.jga.domain.model.generic.CompositeId;

public interface OrganizationDeleteOutput {
    Integer delete(CompositeId tenantId, CompositeId orgId);
}
