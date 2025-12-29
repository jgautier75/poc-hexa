package com.acme.jga.rest.services.organizations.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.organizations.OrganizationCreateInput;
import com.acme.jga.domain.input.functions.organizations.OrganizationDeleteInput;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.organizations.OrganizationUpdateInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.rest.dtos.v1.organizations.OrganizationDto;
import com.acme.jga.rest.dtos.v1.organizations.OrganizationListDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.services.organizations.api.AppOrganizationsService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppOrganizationsServiceImpl implements AppOrganizationsService {
    private final TenantFindInput tenantFindInput;
    private final OrganizationCreateInput organizationCreateInput;
    private final OrganizationFindInput organizationFindInput;
    private final OrganizationUpdateInput organizationUpdateInput;
    private final OrganizationDeleteInput organizationDeleteInput;
    private final ObservationRegistry observationRegistry;

    public AppOrganizationsServiceImpl(TenantFindInput tenantFindInput, OrganizationCreateInput organizationCreateInput,
                                       OrganizationFindInput organizationFindInput, OrganizationUpdateInput organizationUpdateInput,
                                       OrganizationDeleteInput organizationDeleteInput, ObservationRegistry observationRegistry) {
        this.tenantFindInput = tenantFindInput;
        this.organizationCreateInput = organizationCreateInput;
        this.organizationFindInput = organizationFindInput;
        this.organizationUpdateInput = organizationUpdateInput;
        this.organizationDeleteInput = organizationDeleteInput;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public UidDto createOrganization(String tenantUid, OrganizationDto organizationDto) throws FunctionalException {
        Organization organization = new Organization(null, new CompositeId(null, tenantUid), organizationDto.getLabel(),
                organizationDto.getCode(), organizationDto.getKind(), organizationDto.getCountry(), organizationDto.getStatus());
        CompositeId compositeId = organizationCreateInput.create(organization);
        return new UidDto(compositeId.externalId());
    }

    @Override
    public OrganizationListDisplayDto listOrganizations(String tenantUid, Observation parentObservation) throws FunctionalException {
        Observation svcObservation = Observation.createNotStarted("ORGS_SVC_LIST", observationRegistry);
        svcObservation.parentObservation(parentObservation);
        svcObservation.start();
        try {
            List<Organization> orgs = this.organizationFindInput.findAll(new CompositeId(null, tenantUid), svcObservation);
            List<OrganizationDto> dtosOrgs = orgs.stream().map(org -> new OrganizationDto(org.id().externalId(), org.code(), org.label(), org.kind(), org.country(), org.status())).toList();
            return new OrganizationListDisplayDto(dtosOrgs);
        } finally {
            svcObservation.stop();
        }
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
