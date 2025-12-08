package com.acme.jga.domain.output.functions.tenants;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;

public interface TenantCreate {
    CompositeId save(Tenant tenant);
}
