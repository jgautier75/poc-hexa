package com.acme.jga.domain.ports.primary.organizations;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;

public interface OrganizationDeleteInput {
    void delete(CompositeId tenantId, CompositeId orgId) throws FunctionalException;
}
