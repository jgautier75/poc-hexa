package com.acme.jga.domain.functions.users.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.functions.events.builders.users.EventUserHolder;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.input.functions.users.UserDeleteInput;
import com.acme.jga.domain.input.functions.users.UserFindInput;
import com.acme.jga.domain.model.event.*;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserDeleteOutput;
import com.acme.jga.domain.security.holders.ContextUserHolder;

import java.util.List;

@DomainService
public class UserDeleteFuncImpl extends UserEventFunc implements UserDeleteInput {
    private final UserFindInput userFindInput;
    private final UserDeleteOutput userDeleteOutput;
    private final EventPublisher eventPublisher;
    private final ContextUserHolder contextUserHolder;
    private final TenantFindInput tenantFindInput;
    private final OrganizationFindInput organizationFindInput;

    public UserDeleteFuncImpl(UserFindInput userFindInput,
                              UserDeleteOutput userDeleteOutput,
                              EventPublisher eventPublisher,
                              ContextUserHolder contextUserHolder,
                              TenantFindInput tenantFindInput,
                              OrganizationFindInput organizationFindInput) {
        this.userFindInput = userFindInput;
        this.userDeleteOutput = userDeleteOutput;
        this.eventPublisher = eventPublisher;
        this.contextUserHolder = contextUserHolder;
        this.tenantFindInput = tenantFindInput;
        this.organizationFindInput = organizationFindInput;
    }

    @Override
    public Integer delete(CompositeId tenantId, CompositeId organizationId, CompositeId id) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(tenantId);
        Organization organization = organizationFindInput.findById(tenant.id(), organizationId);

        User user = this.userFindInput.findById(tenant.id(), organization.id(), id);

        List<AuditChange> auditChanges = EventUserHolder.getInstance().build(user, null);
        EventData eventData = super.buildEventData(contextUserHolder.getCurrentUser(), AuditAction.DELETE, id.externalId(), tenant, organization, auditChanges);

        Integer nbDeleted = userDeleteOutput.delete(tenant.id(), user.organizationId(), user.id(), eventData);
        this.eventPublisher.pushAuditEvents();
        return nbDeleted;
    }

}
