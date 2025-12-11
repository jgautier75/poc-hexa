package com.acme.jga.domain.model.organization;

import com.acme.jga.domain.model.generic.CompositeId;

public record Organization(CompositeId id, CompositeId tenantId, String label, String code, OrganizationKind kind,
                           String country, OrganizationStatus status) {
}
