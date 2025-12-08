package com.acme.jga.spi.jdbc.model;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.TenantStatus;

public record RdbmsTenant(CompositeId compositeId, String code, String label, TenantStatus tenantStatus) {
}
