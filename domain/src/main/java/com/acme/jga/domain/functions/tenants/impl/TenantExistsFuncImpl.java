package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.otel.OpenTelemetryWrapper;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.TracerProvider;

import java.util.Map;

@DomainService
public class TenantExistsFuncImpl extends OpenTelemetryWrapper implements TenantExistsInput {
    private static final String INSTRUMENTATION_NAME = TenantExistsFuncImpl.class.getCanonicalName();
    private final TenantExistsOutput tenantExistsOutput;

    public TenantExistsFuncImpl(TenantExistsOutput tenantExistsOutput, TracerProvider tracerProvider) {
        super(tracerProvider);
        this.tenantExistsOutput = tenantExistsOutput;
    }

    @Override
    public boolean existsByCode(String code) {
        return tenantExistsOutput.existsByCode(code);
    }

    @Override
    public boolean existsByExternalId(String externalId, Span parentSpan) throws FunctionalException {
        return super.executeWithSpan(INSTRUMENTATION_NAME,
                "TENANTS_DOMAIN_EXIST",
                Map.of("uuid", externalId),
                parentSpan,
                (span) -> tenantExistsOutput.existsByExternalId(externalId, span));
    }

    @Override
    public boolean existsById(CompositeId compositeId, Span parentSpan) throws FunctionalException {
        return super.executeWithSpan(INSTRUMENTATION_NAME,"TENANTS_DOMAIN_EXISTS_ID",Map.of("id",compositeId.toString()),parentSpan,
                (span) -> {
                    return tenantExistsOutput.existsById(compositeId, span);
                });
    }
}
