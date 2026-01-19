package com.acme.jga.rest.adapters.organizations.api;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.organizations.OrganizationDto;
import com.acme.jga.rest.dtos.v1.organizations.OrganizationListDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.Map;

public interface AppOrganizationsService {

    UidDto createOrganization(String tenantUid, OrganizationDto organizationDto) throws FunctionalException;

    OrganizationListDisplayDto listOrganizations(String tenantUid, Map<SearchParams, Object> searchParams) throws FunctionalException;

    OrganizationDto findOrganization(String tenantUid, String organizationUid) throws FunctionalException;

    void updateOrganization(String tenantUid, String orgUid, OrganizationDto organizationDto) throws FunctionalException;

    void deleteOrganization(String tenantUid, String orgUid) throws FunctionalException;
}
