package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.otel.OpenTelemetryWrapper;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.TracerProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TenantExistsOutputImpl extends OpenTelemetryWrapper implements TenantExistsOutput {
    private static final String INSTRUMENTATION_NAME = TenantExistsOutputImpl.class.getCanonicalName();
    private final TenantsDao tenantsDao;

    public TenantExistsOutputImpl(TenantsDao tenantsDao, TracerProvider tracerProvider) {
        super(tracerProvider);
        this.tenantsDao = tenantsDao;
    }

    @Override
    public boolean existsByCode(String code) {
        return this.tenantsDao.existsByCode(code);
    }

    @Override
    public boolean existsByExternalId(String externalId, Span parentSpan) throws FunctionalException {
        return super.executeWithSpan(INSTRUMENTATION_NAME,
                "TENANTS_SPI_EXISTS",
                Map.of("external_id", externalId),
                parentSpan,
                (span) -> this.tenantsDao.existsByExternalId(externalId, parentSpan));
    }

    @Override
    public boolean existsById(CompositeId compositeId, Span parentSpan) throws FunctionalException {
        return super.executeWithSpan(INSTRUMENTATION_NAME,"TENANTS_DAO_EXISTS_ID",Map.of("id",compositeId.toString()),parentSpan,(span) -> {
            return this.tenantsDao.existsById(compositeId);
        });
    }
}
