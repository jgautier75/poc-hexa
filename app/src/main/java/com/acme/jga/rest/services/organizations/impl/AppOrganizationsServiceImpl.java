package com.acme.jga.rest.services.organizations.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.organizations.OrganizationCreateInput;
import com.acme.jga.domain.input.functions.organizations.OrganizationDeleteInput;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.organizations.OrganizationUpdateInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.PaginatedResults;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.rest.dtos.shared.Pagination;
import com.acme.jga.rest.dtos.v1.organizations.OrganizationDto;
import com.acme.jga.rest.dtos.v1.organizations.OrganizationListDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.services.organizations.api.AppOrganizationsService;
import com.acme.jga.search.filtering.constants.SearchParams;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppOrganizationsServiceImpl implements AppOrganizationsService {
    private final OrganizationCreateInput organizationCreateInput;
    private final OrganizationFindInput organizationFindInput;
    private final OrganizationUpdateInput organizationUpdateInput;
    private final OrganizationDeleteInput organizationDeleteInput;

    public AppOrganizationsServiceImpl(OrganizationCreateInput organizationCreateInput,
                                       OrganizationFindInput organizationFindInput, OrganizationUpdateInput organizationUpdateInput,
                                       OrganizationDeleteInput organizationDeleteInput) {
        this.organizationCreateInput = organizationCreateInput;
        this.organizationFindInput = organizationFindInput;
        this.organizationUpdateInput = organizationUpdateInput;
        this.organizationDeleteInput = organizationDeleteInput;
    }

    @Override
    public UidDto createOrganization(String tenantUid, OrganizationDto organizationDto) throws FunctionalException {
        Organization organization = new Organization(null, new CompositeId(null, tenantUid), organizationDto.getLabel(),
                organizationDto.getCode(), organizationDto.getKind(), organizationDto.getCountry(), organizationDto.getStatus());
        CompositeId compositeId = organizationCreateInput.create(organization);
        return new UidDto(compositeId.externalId());
    }

    @Override
    public OrganizationListDisplayDto listOrganizations(String tenantUid, Map<SearchParams, Object> searchParams) throws FunctionalException {
        PaginatedResults<Organization> orgs = this.organizationFindInput.findAll(new CompositeId(null, tenantUid), searchParams);
        List<OrganizationDto> dtosOrgs = orgs.results().stream().map(org -> new OrganizationDto(org.id().externalId(), org.code(), org.label(), org.kind(), org.country(), org.status())).toList();
        return new OrganizationListDisplayDto(dtosOrgs, new Pagination(orgs.nbResults(), (Integer) searchParams.get(SearchParams.PAGE_INDEX), orgs.nbPages()));
    }

    @Override
    public OrganizationDto findOrganization(String tenantUid, String organizationUid) throws FunctionalException {
        Organization org = this.organizationFindInput.findById(new CompositeId(null, tenantUid), new CompositeId(null, organizationUid));
        return new OrganizationDto(org.id().externalId(), org.code(), org.label(), org.kind(), org.country(), org.status());
    }

    @Override
    public void updateOrganization(String tenantUid, String orgUid, OrganizationDto organizationDto) throws FunctionalException {
        Organization organization = new Organization(new CompositeId(null, orgUid), new CompositeId(null, tenantUid), organizationDto.getLabel(),
                organizationDto.getCode(), organizationDto.getKind(), organizationDto.getCountry(), organizationDto.getStatus());
        this.organizationUpdateInput.update(organization);
    }

    @Override
    public void deleteOrganization(String tenantUid, String orgUid) throws FunctionalException {
        this.organizationDeleteInput.delete(new CompositeId(null, tenantUid), new CompositeId(null, orgUid));
    }
}
