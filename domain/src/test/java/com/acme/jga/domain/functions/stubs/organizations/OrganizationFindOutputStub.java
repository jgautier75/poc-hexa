package com.acme.jga.domain.functions.stubs.organizations;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.ports.output.organizations.OrganizationFindOutput;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrganizationFindOutputStub implements OrganizationFindOutput {
    public static final CompositeId DEFAULT_ORG_ID = new CompositeId(1L, UUID.randomUUID().toString());
    public static final List<Organization> ORGANIZATIONS = new ArrayList<>();

    public OrganizationFindOutputStub(Tenant tenant) {
        Organization org = new Organization(DEFAULT_ORG_ID, tenant.id(), "deforg", "deforg", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        ORGANIZATIONS.add(org);
    }

    @Override
    public Organization findById(CompositeId tenantId, CompositeId organizationId) {
        return ORGANIZATIONS.stream().filter(org -> org.tenantId().internalId().equals(tenantId.internalId())
                && org.id().internalId().equals(organizationId.internalId())
        ).findFirst().orElse(null);
    }

    @Override
    public boolean existsByCode(String code) {
        return ORGANIZATIONS.stream().anyMatch(org -> org.code().equals(code));
    }

    @Override
    public List<Organization> findAll(CompositeId tenantId, Map<SearchParams, Object> searchParams) {
        return ORGANIZATIONS;
    }

    @Override
    public Integer countAll(CompositeId tenantId, Map<SearchParams, Object> searchParams) {
        return ORGANIZATIONS.size();
    }

    @Override
    public Organization findByCode(String code) {
        return ORGANIZATIONS.stream().filter(org -> org.code().equals(code)).findFirst().orElse(null);
    }

    public List<Organization> orgsStub() {
        return ORGANIZATIONS;
    }
}
