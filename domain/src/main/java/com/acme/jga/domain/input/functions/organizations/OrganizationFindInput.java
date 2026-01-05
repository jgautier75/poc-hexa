package com.acme.jga.domain.input.functions.organizations;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.PaginatedResults;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.Map;

public interface OrganizationFindInput {

    PaginatedResults<Organization> findAll(CompositeId tenantId, Map<SearchParams, Object> searchParams) throws FunctionalException;

    Organization findById(CompositeId tenantId, CompositeId organizationId) throws FunctionalException;

}
