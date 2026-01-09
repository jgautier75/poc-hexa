package com.acme.jga.domain.functions.users.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.functions.events.builders.events.EventUserHolder;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.input.functions.users.UserFindInput;
import com.acme.jga.domain.input.functions.users.UserUpdateInput;
import com.acme.jga.domain.model.event.*;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserUpdateOutput;
import com.acme.jga.domain.security.holders.ContextUserHolder;

import java.util.List;

import static com.acme.jga.domain.functions.users.validation.UserUpdateValidationHolder.getInstance;

@DomainService
public class UserUpdateFuncImpl implements UserUpdateInput {
    private final TenantFindInput tenantFindInput;
    private final OrganizationFindInput organizationFindInput;
    private final UserFindInput userFindInput;
    private final UserUpdateOutput userUpdateOutput;
    private final ContextUserHolder contextUserHolder;
    private final EventPublisher eventPublisher;

    public UserUpdateFuncImpl(TenantFindInput tenantFindInput,
                              OrganizationFindInput organizationFindInput,
                              UserFindInput userFindInput,
                              UserUpdateOutput userUpdateOutput,
                              ContextUserHolder contextUserHolder,
                              EventPublisher eventPublisher) {
        this.tenantFindInput = tenantFindInput;
        this.organizationFindInput = organizationFindInput;
        this.userFindInput = userFindInput;
        this.userUpdateOutput = userUpdateOutput;
        this.contextUserHolder = contextUserHolder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Integer update(User user) throws FunctionalException {
        getInstance().validate(user);
        Tenant tenant = tenantFindInput.findById(user.tenantId());
        Organization organization = organizationFindInput.findById(tenant.id(), user.organizationId());
        User rdbmsUser = userFindInput.findById(tenant.id(), organization.id(), user.id());
        User updateUser = new User(null, tenant.id(), organization.id(), user.login(), user.firstName(), user.lastName(), user.middleName(), user.email(), user.status(), user.notifEmail(), null);
        List<AuditChange> auditChanges = EventUserHolder.getInstance().build(rdbmsUser, updateUser);
        EventData eventData = buildEventData(tenant, organization, updateUser.id().externalId(), auditChanges);
        Integer nbUpdated = userUpdateOutput.update(updateUser, eventData);
        this.eventPublisher.pushAuditEvents();
        return nbUpdated;
    }

    private EventData buildEventData(Tenant tenant, Organization org, String userUid, List<AuditChange> auditChanges) {
        AuditScope scope = new AuditScope().toBuilder()
                .tenantName(tenant.code())
                .tenantUid(tenant.id().externalId())
                .organizationName(org.code())
                .organizationUid(org.id().externalId())
                .build();
        return new EventData(contextUserHolder.getCurrentUser(), userUid, scope, AuditAction.UPDATE, EventTarget.USER, auditChanges);
    }
}
