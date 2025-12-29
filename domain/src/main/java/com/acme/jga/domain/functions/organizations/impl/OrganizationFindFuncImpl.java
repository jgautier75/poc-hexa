package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.otel.OpenTelemetryWrapper;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.TracerProvider;

import java.util.List;
import java.util.Map;

@DomainService
public class OrganizationFindFuncImpl extends OpenTelemetryWrapper implements OrganizationFindInput {
    private static final String INSTRUMENTATION_NAME = OrganizationFindFuncImpl.class.getCanonicalName();
    private final OrganizationFindOutput organizationFindOutput;
    private final TenantFindInput tenantFindInput;

    public OrganizationFindFuncImpl(OrganizationFindOutput organizationFindOutput, TenantFindInput tenantFindInput, TracerProvider tracerProvider) {
        super(tracerProvider);
        this.organizationFindOutput = organizationFindOutput;
        this.tenantFindInput = tenantFindInput;
    }

    @Override
    public List<Organization> findAll(CompositeId tenantId, Span parentSpan) throws FunctionalException {
        return super.executeWithSpan(INSTRUMENTATION_NAME, "ORGS_DOMAIN_LIST", Map.of("tenant", tenantId.toString()), parentSpan, (span) -> {
            Tenant tenant = tenantFindInput.findById(tenantId, span);
            return organizationFindOutput.findAll(tenant.id(), span);
        });
    }

    @Override
    public Organization findById(CompositeId tenantId, CompositeId organizationId) throws FunctionalException {
        return super.executeWithSpan(INSTRUMENTATION_NAME, "ORGS_DOMAIN_BY_ID", Map.of("tenant_id", tenantId.toString(), "org_id", organizationId.toString()), null, (span) -> {
            Tenant tenant = tenantFindInput.findById(tenantId, span);
            Organization org = this.organizationFindOutput.findById(tenant.id(), organizationId);
            if (org == null) {
                throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("organization.not_found", organizationId.externalId()));
            }
            return org;
        });

    }
}
