package com.acme.jga.domain.functions.users.impl;

import com.acme.jga.domain.model.event.*;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;

import java.util.List;

public abstract class UserEventFunc {

    protected EventData buildEventData(String currentUser, AuditAction auditAction, String uuid, Tenant tenant, Organization org, List<AuditChange> auditChanges) {
        AuditScope scope = new AuditScope().toBuilder()
                .tenantName(tenant.code())
                .tenantUid(tenant.id().externalId())
                .organizationName(org.code())
                .organizationUid(org.id().externalId())
                .build();
        return new EventData(currentUser, uuid, scope, auditAction, EventTarget.USER, auditChanges);
    }
}
