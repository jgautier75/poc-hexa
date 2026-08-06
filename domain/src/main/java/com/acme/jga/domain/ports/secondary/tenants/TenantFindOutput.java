package com.acme.jga.domain.ports.secondary.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;

public interface TenantFindOutput {
    Tenant findByCode(String code);
    Tenant findByExternalId(CompositeId tenantId);
    Tenant findById(CompositeId tenantId) throws FunctionalException;
}
