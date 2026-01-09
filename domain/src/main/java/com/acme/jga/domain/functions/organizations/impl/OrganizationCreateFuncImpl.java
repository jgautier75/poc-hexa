package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.events.builders.organizations.EventOrganizationHolder;
import com.acme.jga.domain.functions.organizations.validation.OrganizationCreateValidationHolder;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationCreateInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.event.*;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationCreateOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;
import com.acme.jga.domain.security.holders.ContextUserHolder;

import java.util.Collections;
import java.util.List;

@DomainService
public class OrganizationCreateFuncImpl implements OrganizationCreateInput {
    private final TenantExistsInput tenantExistsFunc;
    private final TenantFindInput tenantFindInput;
    private final OrganizationFindOutput organizationFindOutput;
    private final OrganizationCreateOutput organizationCreateOutput;
    private final EventPublisher eventPublisher;
    private final ContextUserHolder contextUserHolder;

    public OrganizationCreateFuncImpl(TenantExistsInput tenantExistsFunc,
                                      TenantFindInput tenantFindInput,
                                      OrganizationFindOutput organizationFindOutput,
                                      OrganizationCreateOutput organizationCreateOutput,
                                      EventPublisher eventPublisher, ContextUserHolder contextUserHolder) {
        this.tenantExistsFunc = tenantExistsFunc;
        this.tenantFindInput = tenantFindInput;
        this.organizationFindOutput = organizationFindOutput;
        this.organizationCreateOutput = organizationCreateOutput;
        this.eventPublisher = eventPublisher;
        this.contextUserHolder = contextUserHolder;
    }

    @Override
    public CompositeId create(Organization organization) throws FunctionalException {
        // Ensure tenant exists
        boolean tenantExists = tenantExistsFunc.existsByExternalId(organization.tenantId().externalId());
        if (!tenantExists) {
            throw new FunctionalException(Scope.TENANT.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("tenant.not_found", organization.tenantId().externalId()));
        }

        // Ensure organization code is not already used
        boolean orgCodeExists = organizationFindOutput.existsByCode(organization.code());
        if (orgCodeExists) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.ALREADY_EXISTS.name(), BundleFactory.getMessage("organization.code_already_used", organization.code()));
        }

        // Validate payload
        OrganizationCreateValidationHolder.getInstance().validate(organization);

        Tenant tenant = tenantFindInput.findById(organization.tenantId());

        Organization org = new Organization(null, tenant.id(), organization.label(), organization.code(), organization.kind(), organization.country(), organization.status());
        List<AuditChange> auditChanges = EventOrganizationHolder.getInstance().build(null, org);
        EventData eventData = buildEventData(tenant, auditChanges);

        Sector rootSector = new Sector(null, tenant.id(), null, org.label(), org.code(), null, true, Collections.emptyList());
        CompositeId orgId = organizationCreateOutput.save(org, rootSector, eventData);

        this.eventPublisher.pushAuditEvents();
        return orgId;
    }

    private EventData buildEventData(Tenant tenant, List<AuditChange> auditChanges) {
        AuditScope scope = new AuditScope().toBuilder()
                .tenantName(tenant.code())
                .tenantUid(tenant.id().externalId())
                .build();
        return new EventData(contextUserHolder.getCurrentUser(), null, scope, AuditAction.CREATE, EventTarget.ORGANIZATION, auditChanges);
    }

}
