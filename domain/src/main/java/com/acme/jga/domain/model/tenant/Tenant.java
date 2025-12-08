package com.acme.jga.domain.model;

public record Tenant(Long internalId, String externalId, String code, String label,
                     TenantStatus tenantStatus) implements ExternalId {
    @Override
    public String getExternalId() {
        return externalId;
    }
}
