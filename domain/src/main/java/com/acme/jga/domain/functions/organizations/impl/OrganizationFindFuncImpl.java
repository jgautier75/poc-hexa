package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.micrometer.MicrometerWrapper;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.PaginatedResults;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.search.SearchUtilities;
import com.acme.jga.search.filtering.constants.SearchParams;
import com.acme.jga.search.filtering.utils.ParsingResult;
import io.micrometer.observation.ObservationRegistry;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@DomainService
public class OrganizationFindFuncImpl extends MicrometerWrapper implements OrganizationFindInput {
    private final OrganizationFindOutput organizationFindOutput;
    private final TenantFindInput tenantFindInput;

    public OrganizationFindFuncImpl(OrganizationFindOutput organizationFindOutput,
                                    TenantFindInput tenantFindInput, ObservationRegistry observationRegistry, SdkLoggerProvider sdkLoggerProvider) {
        super(observationRegistry, sdkLoggerProvider);
        this.organizationFindOutput = organizationFindOutput;
        this.tenantFindInput = tenantFindInput;
    }

    @Override
    public PaginatedResults<Organization> findAll(CompositeId tenantId, Map<SearchParams, Object> searchParams) throws FunctionalException {
        super.log("Listing organizations for tenant [" + tenantId + "]", null);
        Tenant tenant = tenantFindInput.findById(tenantId);
        Map<SearchParams, Object> params = SearchUtilities.checkParameters(searchParams);
        Integer nbOrgs = organizationFindOutput.countAll(tenant.id(), params);
        List<Organization> orgs = organizationFindOutput.findAll(tenant.id(), searchParams);
        return new PaginatedResults<>(nbOrgs,
                nbOrgs != null ? (nbOrgs / (Integer) searchParams.get(SearchParams.PAGE_SIZE) + 1) : 0,
                orgs,
                (Integer) searchParams.get(SearchParams.PAGE_INDEX),
                (Integer) searchParams.get(SearchParams.PAGE_SIZE)
        );
    }

    @Override
    public Organization findById(CompositeId tenantId, CompositeId organizationId) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(tenantId);
        Organization organization = this.organizationFindOutput.findById(tenant.id(), organizationId);
        return Optional
                .of(organization)
                .orElseThrow(() -> new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("organization.not_found", organizationId.externalId())));
    }
}
