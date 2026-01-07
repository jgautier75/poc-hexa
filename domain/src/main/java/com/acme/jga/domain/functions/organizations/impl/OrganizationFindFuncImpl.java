package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.PaginatedResults;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.search.SearchUtilities;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@DomainService
public class OrganizationFindFuncImpl implements OrganizationFindInput {
    private final OrganizationFindOutput organizationFindOutput;
    private final TenantFindInput tenantFindInput;

    public OrganizationFindFuncImpl(OrganizationFindOutput organizationFindOutput,
                                    TenantFindInput tenantFindInput) {
        this.organizationFindOutput = organizationFindOutput;
        this.tenantFindInput = tenantFindInput;
    }

    @Override
    public PaginatedResults<Organization> findAll(CompositeId tenantId, Map<SearchParams, Object> searchParams) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(tenantId);
        Map<SearchParams, Object> computedSearchParams = SearchUtilities.checkParameters(searchParams);
        Integer nbOrgs = organizationFindOutput.countAll(tenant.id(), computedSearchParams);
        List<Organization> orgs = organizationFindOutput.findAll(tenant.id(), computedSearchParams);
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
