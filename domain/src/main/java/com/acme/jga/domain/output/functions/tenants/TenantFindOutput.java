package com.acme.jga.domain.output.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import io.opentelemetry.api.trace.Span;

public interface TenantFindOutput {
    Tenant findByCode(String code);
    Tenant findByExternalId(CompositeId tenantId);
    Tenant findById(CompositeId tenantId, Span parentSpan) throws FunctionalException;
}
