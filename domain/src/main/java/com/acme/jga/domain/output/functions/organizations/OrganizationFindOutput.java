package com.acme.jga.domain.output.functions.organizations;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.search.filtering.constants.SearchParams;
import io.opentelemetry.api.trace.Span;

import java.util.List;
import java.util.Map;

public interface OrganizationFindOutput {

    Organization findById(CompositeId tenantId, CompositeId organizationId);

    boolean existsByCode(String code);

    List<Organization> findAll(CompositeId tenantId,Map<SearchParams, Object> searchParams) throws FunctionalException;

    Integer countAll(CompositeId tenantId, Map<SearchParams, Object> searchParams);

    Organization findByCode(String code);
}
