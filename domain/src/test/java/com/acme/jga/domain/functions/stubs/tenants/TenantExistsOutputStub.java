package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;
import io.opentelemetry.api.trace.Span;

import java.util.List;

public class TenantExistsOutputStub implements TenantExistsOutput {

    private final List<Tenant> tenants;

    public TenantExistsOutputStub(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    @Override
    public boolean existsByCode(String code) {
        return tenants.stream().anyMatch(t -> t.code().equals(code));
    }

    @Override
    public boolean existsByExternalId(String externalId, Span parentSpan) {
        return tenants.stream().anyMatch(t -> t.id().externalId().equals(externalId));
    }

    @Override
    public boolean existsById(CompositeId compositeId, Span parentSpan) {
        if (compositeId.kind() == IdKind.BOTH && compositeId.internalId() != null) {
            return tenants.stream().anyMatch(t -> t.id().internalId().longValue() == compositeId.internalId().longValue());
        } else if (compositeId.externalId() != null) {
            return tenants.stream().anyMatch(t -> t.id().externalId().equals(compositeId.externalId()));
        } else {
            return false;
        }
    }
}
