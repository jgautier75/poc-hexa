package com.acme.jga.domain.functions.stubs.organizations;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.output.functions.organizations.OrganizationCreateOutput;

import java.util.UUID;

public class OrganizationCreateOutputStub implements OrganizationCreateOutput {
    @Override
    public CompositeId save(Organization organization) {
        return new CompositeId(1L, UUID.randomUUID().toString());
    }

    @Override
    public CompositeId save(Organization organization, Sector rootSector) {
        return new CompositeId(1L, UUID.randomUUID().toString());
    }
}
