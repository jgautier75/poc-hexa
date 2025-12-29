package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.otel.OpenTelemetryWrapper;
import com.acme.jga.domain.output.functions.tenants.TenantFindOutput;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import io.micrometer.observation.annotation.Observed;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.TracerProvider;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class TenantFindOutputImpl extends OpenTelemetryWrapper implements TenantFindOutput {
    private static final String INSTRUMENTATION_NAME = TenantFindOutputImpl.class.getCanonicalName();
    private final TenantsDao tenantsDao;

    public TenantFindOutputImpl(TenantsDao tenantsDao, TracerProvider tracerProvider) {
        super(tracerProvider);
        this.tenantsDao = tenantsDao;
    }

    @Override
    public Tenant findByCode(String code) {
        Tenant rdbmsTenant = this.tenantsDao.findByCode(code);
        return Optional.ofNullable(rdbmsTenant).map(rt -> new Tenant(rdbmsTenant.id(), rdbmsTenant.code(), rdbmsTenant.label(), rdbmsTenant.status())).orElse(null);
    }

    @Override
    public Tenant findByExternalId(CompositeId tenantId) {
        Tenant rdbmsTenant = this.tenantsDao.findByExternalId(tenantId.get());
        return Optional.ofNullable(rdbmsTenant).map(rt -> new Tenant(rdbmsTenant.id(), rdbmsTenant.code(), rdbmsTenant.label(), rdbmsTenant.status())).orElse(null);
    }

    @Override
    public Tenant findById(CompositeId tenantId, Span parentSpan) throws FunctionalException {
        return super.executeWithSpan(INSTRUMENTATION_NAME, "TENANTS_SPI_BY_ID", Map.of("tenant_id", tenantId.toString()), parentSpan, (span) -> {
            return this.tenantsDao.findById(tenantId);
        });
    }
}
