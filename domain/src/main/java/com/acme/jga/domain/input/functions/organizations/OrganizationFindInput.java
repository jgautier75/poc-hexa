package com.acme.jga.domain.input.functions.organizations;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.search.filtering.constants.SearchParams;
import io.opentelemetry.api.trace.Span;

import java.util.List;
import java.util.Map;

public interface OrganizationFindInput {

    List<Organization> findAll(CompositeId tenantId) throws FunctionalException;

    Organization findById(CompositeId tenantId, CompositeId organizationId) throws FunctionalException;

}
