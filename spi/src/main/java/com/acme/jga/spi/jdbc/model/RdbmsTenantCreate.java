package com.acme.jga.spi.jdbc.model;

import com.acme.jga.domain.model.tenant.TenantStatus;

public record RdbmsTenantCreate(String code, String label, TenantStatus status) {
}
