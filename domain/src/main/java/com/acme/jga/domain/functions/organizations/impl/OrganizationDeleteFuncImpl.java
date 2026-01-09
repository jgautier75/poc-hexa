package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.events.builders.organizations.EventOrganizationHolder;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationDeleteInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.event.*;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationDeleteOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.security.holders.ContextUserHolder;

import java.util.List;
import java.util.Optional;

@DomainService
public class OrganizationDeleteFuncImpl implements OrganizationDeleteInput {
    private final TenantFindInput tenantFindInput;
    private final OrganizationFindOutput organizationFindOutput;
    private final OrganizationDeleteOutput organizationDeleteOutput;
    private final ContextUserHolder contextUserHolder;
    private final EventPublisher eventPublisher;

    public OrganizationDeleteFuncImpl(TenantFindInput tenantFindInput,
                                      OrganizationFindOutput organizationFindOutput,
                                      OrganizationDeleteOutput organizationDeleteOutput,
                                      ContextUserHolder contextUserHolder,
                                      EventPublisher eventPublisher) {
        this.tenantFindInput = tenantFindInput;
        this.organizationFindOutput = organizationFindOutput;
        this.organizationDeleteOutput = organizationDeleteOutput;
        this.contextUserHolder = contextUserHolder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void delete(CompositeId tenantId, CompositeId orgId) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(tenantId);
        Organization orgDb = this.organizationFindOutput.findById(tenantId, orgId);
        if (orgDb == null) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("organization.not_found", orgId.externalId()));
        }
        List<AuditChange> auditChanges = EventOrganizationHolder.getInstance().build(orgDb, null);
        EventData eventData = buildEventData(tenant, orgDb.id().externalId(), auditChanges);
        this.organizationDeleteOutput.delete(tenantId, orgDb.id(), eventData);
        eventPublisher.pushAuditEvents();
    }

    private EventData buildEventData(Tenant tenant, String orgUid, List<AuditChange> auditChanges) {
        AuditScope scope = new AuditScope().toBuilder()
                .tenantName(tenant.code())
                .tenantUid(tenant.id().externalId())
                .build();
        return new EventData(contextUserHolder.getCurrentUser(), orgUid, scope, AuditAction.DELETE, EventTarget.ORGANIZATION, auditChanges);
    }
}
