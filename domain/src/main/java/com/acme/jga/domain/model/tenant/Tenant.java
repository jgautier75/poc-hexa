package com.acme.jga.domain.model.tenant;

public record Tenant(TenantId tenantId, String code, String label,
                     TenantStatus tenantStatus) implements TenantId {
    @Override
    public String get() {
        return tenantId.get();
    }
}
