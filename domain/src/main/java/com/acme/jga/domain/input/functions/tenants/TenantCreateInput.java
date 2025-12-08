package com.acme.jga.domain.input.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.tenant.TenantId;
import com.acme.jga.domain.model.tenant.Tenant;

public interface TenantCreateInput {
    TenantId create(Tenant tenant) throws FunctionalException;
}
