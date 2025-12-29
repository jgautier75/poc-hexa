package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.otel.OpenTelemetryWrapper;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.TracerProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrganizationFindOutputImpl extends OpenTelemetryWrapper implements OrganizationFindOutput {
    private static final String INSTRUMENTATION_NAME = OrganizationFindOutputImpl.class.getCanonicalName();
    private final OrganizationsDao organizationsDao;

    public OrganizationFindOutputImpl(OrganizationsDao organizationsDao, TracerProvider tracerProvider) {
        super(tracerProvider);
        this.organizationsDao = organizationsDao;
    }

    @Override
    public Organization findById(CompositeId tenantId, CompositeId organizationId) {
        return this.organizationsDao.findByTenantAndId(tenantId, organizationId);
    }

    @Override
    public boolean existsByCode(String code) {
        return this.organizationsDao.existsByCode(code);
    }

    @Override
    public List<Organization> findAll(CompositeId tenantId, Span parentSpan) throws FunctionalException {
        return super.executeWithSpan(INSTRUMENTATION_NAME, "ORGS_SPI_LIST", Map.of("tenant_id", tenantId.toString()), parentSpan, (span) -> {
            return this.organizationsDao.findAll(tenantId, span);
        });
    }

    @Override
    public Organization findByCode(String code) {
        return this.organizationsDao.findByCode(code);
    }
}
