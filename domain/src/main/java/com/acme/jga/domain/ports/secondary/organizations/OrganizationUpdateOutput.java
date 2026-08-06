package com.acme.jga.domain.ports.secondary.organizations;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.organization.Organization;

public interface OrganizationUpdateOutput {
    Integer update(Organization organization, EventData eventData);
}
