package com.acme.jga.domain.ports.input.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;

public interface TenantCreateInput {
    CompositeId create(Tenant tenant) throws FunctionalException;
}
