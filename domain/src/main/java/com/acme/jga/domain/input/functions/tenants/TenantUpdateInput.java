package com.acme.jga.domain.input.functions.tenants;

import com.acme.jga.domain.model.tenant.Tenant;

public interface TenantUpdateInput {

    boolean update(Tenant tenant);
}
