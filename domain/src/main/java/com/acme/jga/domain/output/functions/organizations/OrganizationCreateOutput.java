package com.acme.jga.domain.output.functions.organizations;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;

public interface OrganizationCreateOutput {
    CompositeId save(Organization organization);

    CompositeId save(Organization organization, Sector rootSector);
}
