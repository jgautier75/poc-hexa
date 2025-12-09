package com.acme.jga.domain.output.functions.tenants;

import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantId;

public interface TenantFindOutput {
    Tenant findByCode(String code);
    Tenant findByExternalId(TenantId tenantId);
}
