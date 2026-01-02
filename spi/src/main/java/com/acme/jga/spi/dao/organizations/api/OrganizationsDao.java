package com.acme.jga.spi.dao.organizations.api;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.List;
import java.util.Map;

public interface OrganizationsDao {

    CompositeId save(Organization org);

    Organization findByTenantAndId(CompositeId tenantId, CompositeId id);

    Integer update(CompositeId tenantId, CompositeId orgId, String code, String label, String country, OrganizationStatus status);

    Integer delete(CompositeId tenantId, CompositeId orgId);

    boolean existsByCode(String code);

    List<Organization> findAll(CompositeId tenantId);

    Organization findByCode(String code);

}
