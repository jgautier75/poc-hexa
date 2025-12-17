package com.acme.jga.domain.functions.stubs.organizations;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.domain.model.tenant.Tenant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrganizationFindInputStub implements OrganizationFindInput {

    public static final CompositeId DEFAULT_ORG_ID = new CompositeId(1L, UUID.randomUUID().toString());
    private static final List<Organization> organizations = new ArrayList<>();

    public OrganizationFindInputStub(Tenant tenant) {
        Organization org = new Organization(DEFAULT_ORG_ID, tenant.id(), "deforg", "deforg", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        organizations.add(org);
    }

    @Override
    public List<Organization> findAll(CompositeId tenantId) throws FunctionalException {
        return organizations;
    }

    @Override
    public Organization findById(CompositeId tenantId, CompositeId organizationId) throws FunctionalException {
        return organizations.stream().filter(org -> org.tenantId().internalId().longValue() == tenantId.internalId().longValue()
                && org.id().internalId().longValue() == organizationId.internalId().longValue()).findFirst().orElseThrow(() -> new FunctionalException(Scope.ORGANIZATION.name(), "tenant_not_found", ""));
    }

    public Organization getOrganization() {
        return organizations.get(0);
    }

}
