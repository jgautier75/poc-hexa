package com.acme.jga.domain.output.functions.organizations;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;

public interface OrganizationCreateOutput {
    CompositeId save(Organization organization);
}
