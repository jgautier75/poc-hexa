package com.acme.jga.domain.model.tenant;

import com.acme.jga.domain.model.generic.CompositeId;

public record Tenant(CompositeId id, String code, String label,
                     TenantStatus status) {

}
