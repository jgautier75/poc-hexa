package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.otel.OpenTelemetryWrapper;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;
import com.acme.jga.domain.output.functions.tenants.TenantFindOutput;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.TracerProvider;

import java.util.Map;

@DomainService
public class TenantFindFuncImpl extends OpenTelemetryWrapper implements TenantFindInput {
    private static final String INSTRUMENTATION_NAME = TenantFindFuncImpl.class.getCanonicalName();
    private final TenantFindOutput tenantFindOutput;
    private final TenantExistsInput tenantExistsFunc;

    public TenantFindFuncImpl(TenantFindOutput tenantFindOutput, TenantExistsInput tenantExistsFunc, TracerProvider tracerProvider) {
        super(tracerProvider);
        this.tenantFindOutput = tenantFindOutput;
        this.tenantExistsFunc = tenantExistsFunc;
    }

    @Override
    public Tenant findByCode(String code) throws FunctionalException {

        // Ensure tenant exists
        boolean exists = tenantExistsFunc.existsByCode(code);
        if (!exists) {
            throwException(code);
        }

        // Fetch tenant
        return tenantFindOutput.findByCode(code);
    }

    @Override
    public Tenant findById(CompositeId tenantId, Span parentSpan) throws FunctionalException {
        return super.executeWithSpan(INSTRUMENTATION_NAME, "TENANTS_DOMAIN_BY_ID", Map.of("tenant_id", tenantId.toString()), parentSpan, (span) -> {
            // Ensure tenant exists
            boolean exists = tenantExistsFunc.existsById(tenantId, span);
            if (!exists) {
                throwException(tenantId.get());
            }

            return tenantFindOutput.findById(tenantId, span);
        });

    }

    private void throwException(String args) throws FunctionalException {
        throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", args));
    }

}
