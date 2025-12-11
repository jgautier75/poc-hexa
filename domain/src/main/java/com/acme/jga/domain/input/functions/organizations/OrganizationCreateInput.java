package com.acme.jga.domain.input.functions.organizations;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;

public interface OrganizationCreateInput {
    CompositeId create(Organization organization) throws FunctionalException;
}
