package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.events.builders.organizations.EventOrganizationHolder;
import com.acme.jga.domain.functions.organizations.validation.OrganizationValidationHolder;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationUpdateInput;
import com.acme.jga.domain.model.event.*;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationUpdateOutput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;
import com.acme.jga.domain.output.functions.tenants.TenantFindOutput;
import com.acme.jga.domain.security.holders.ContextUserHolder;

import java.util.List;

@DomainService
public class OrganizationUpdateFuncImpl implements OrganizationUpdateInput {
    private final TenantFindOutput tenantFindOutput;
    private final TenantExistsOutput tenantExistsOutput;
    private final OrganizationFindOutput organizationFindOutput;
    private final OrganizationUpdateOutput organizationUpdateOutput;
    private final ContextUserHolder contextUserHolder;
    private final EventPublisher eventPublisher;

    public OrganizationUpdateFuncImpl(TenantFindOutput tenantFindOutput,
                                      TenantExistsOutput tenantExistsOutput,
                                      OrganizationFindOutput organizationFindOutput,
                                      OrganizationUpdateOutput organizationUpdateOutput,
                                      ContextUserHolder contextUserHolder,
                                      EventPublisher eventPublisher) {
        this.tenantFindOutput = tenantFindOutput;
        this.tenantExistsOutput = tenantExistsOutput;
        this.organizationFindOutput = organizationFindOutput;
        this.organizationUpdateOutput = organizationUpdateOutput;
        this.contextUserHolder = contextUserHolder;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public void update(Organization organization) throws FunctionalException {

        // Validate
        OrganizationValidationHolder.getInstance().validate(organization);

        boolean tenantExists = this.tenantExistsOutput.existsByExternalId(organization.tenantId().externalId());
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", organization.tenantId().externalId()));
        }

        Tenant tenant = tenantFindOutput.findById(organization.tenantId());

        // Ensure organization code is not already used
        Organization rdbmsOrg = this.organizationFindOutput.findById(tenant.id(), organization.id());
        Organization orgByCode = this.organizationFindOutput.findByCode(organization.code());
        if (orgByCode != null && orgByCode.id().internalId().longValue() != rdbmsOrg.id().internalId().longValue()) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.ALREADY_EXISTS.name(), BundleFactory.getMessage("organization.code_already_used", organization.code()));
        }

        Organization updateOrg = new Organization(rdbmsOrg.id(), tenant.id(), organization.label(), organization.code(), organization.kind(), organization.country(), organization.status());
        List<AuditChange> auditChanges = EventOrganizationHolder.getInstance().build(rdbmsOrg, updateOrg);
        EventData eventData = buildEventData(tenant, updateOrg.id().externalId(), auditChanges);
        this.organizationUpdateOutput.update(updateOrg, eventData);
        this.eventPublisher.pushAuditEvents();
    }

    private EventData buildEventData(Tenant tenant, String orgUid, List<AuditChange> auditChanges) {
        AuditScope scope = new AuditScope().toBuilder()
                .tenantName(tenant.code())
                .tenantUid(tenant.id().externalId())
                .build();
        return new EventData(contextUserHolder.getCurrentUser(), orgUid, scope, AuditAction.UPDATE, EventTarget.ORGANIZATION, auditChanges);
    }

}
