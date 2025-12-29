package com.acme.jga.domain.input.functions.organizations;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import io.opentelemetry.api.trace.Span;

import java.util.List;

public interface OrganizationFindInput {

    List<Organization> findAll(CompositeId tenantId, Span parentSpan) throws FunctionalException;

    Organization findById(CompositeId tenantId, CompositeId organizationId) throws FunctionalException;

}
