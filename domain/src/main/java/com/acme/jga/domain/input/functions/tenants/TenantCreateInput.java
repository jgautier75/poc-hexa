package com.acme.jga.domain.input.functions.tenants;

import com.acme.jga.domain.model.tenant.TenantId;
import com.acme.jga.domain.model.tenant.Tenant;

public interface TenantCreate {
    TenantId create(Tenant tenant);
}
