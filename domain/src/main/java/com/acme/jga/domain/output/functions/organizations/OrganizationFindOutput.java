package com.acme.jga.domain.output.functions.organizations;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import io.micrometer.observation.Observation;

import java.util.List;

public interface OrganizationFindOutput {

    Organization findById(CompositeId tenantId, CompositeId organizationId);

    boolean existsByCode(String code);

    List<Organization> findAll(CompositeId tenantId, Observation parentObservation);

    Organization findByCode(String code);
}
