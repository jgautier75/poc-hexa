package com.acme.jga.domain.ports.primary.organizations;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.organization.Organization;

public interface OrganizationUpdateInput {
    void update(Organization organization) throws FunctionalException;
}
