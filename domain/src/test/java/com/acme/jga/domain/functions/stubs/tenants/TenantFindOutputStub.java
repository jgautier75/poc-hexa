package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantFindOutput;

import java.util.List;

public class TenantFindOutputStub implements TenantFindOutput {
    private final List<Tenant> tenants;

    public TenantFindOutputStub(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    @Override
    public Tenant findByCode(String code) {
        return tenants.stream().filter(t -> t.code().equals(code)).findFirst().orElse(null);
    }

    @Override
    public Tenant findByExternalId(CompositeId tenantId) {
        return tenants.stream().filter(t -> t.id().get().equals(tenantId.get())).findFirst().orElse(null);
    }

    @Override
    public Tenant findById(CompositeId tenantId) {
        if (tenantId != null && tenantId.internalId() != null) {
            return tenants.stream().filter(t -> t.id().internalId().longValue() == tenantId.internalId().longValue()).findFirst().orElse(null);
        } else if (tenantId != null && tenantId.externalId() != null) {
            return tenants.stream().filter(t -> t.id().externalId().equals(tenantId.externalId())).findFirst().orElse(null);
        }
        return null;
    }
}
