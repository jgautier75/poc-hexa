package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.stubs.events.VoidEventPublisherStub;
import com.acme.jga.domain.functions.stubs.organizations.OrganizationCreateOutputStub;
import com.acme.jga.domain.functions.stubs.organizations.OrganizationFindOutputStub;
import com.acme.jga.domain.functions.stubs.security.holders.ContextUserHolderStub;
import com.acme.jga.domain.functions.stubs.tenants.TenantExistsFuncStub;
import com.acme.jga.domain.functions.stubs.tenants.TenantFindInputStub;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.domain.output.functions.organizations.OrganizationCreateOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.domain.output.functions.tenants.TenantExistsInput;
import com.acme.jga.domain.security.holders.ContextUserHolder;
import com.acme.jga.domain.validation.ValidationException;
import com.acme.jga.domain.validation.ValidationRule;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationCreateFuncImplTest {
    private static final CompositeId TENANT_ID = new CompositeId(1L, UUID.randomUUID().toString());
    private static final Tenant TENANT = new Tenant(TENANT_ID, "root-tenant", "root-tenant", TenantStatus.ACTIVE);
    private static final TenantFindInput TENANT_FIND_INPUT = new TenantFindInputStub(TENANT);
    private static final TenantExistsInput TENANT_EXISTS_INPUT = new TenantExistsFuncStub(List.of(TENANT));
    private static final OrganizationFindOutput ORGANIZATION_FIND_OUTPUT = new OrganizationFindOutputStub(TENANT);
    private static final EventPublisher EVENT_PUBLISHER = new VoidEventPublisherStub();
    private static final OrganizationCreateOutput ORGANIZATION_CREATE_OUTPUT = new OrganizationCreateOutputStub();
    private static final ContextUserHolder CONTEXT_USER_HOLDER = new ContextUserHolderStub();
    private static final OrganizationCreateFuncImpl ORGANIZATION_CREATE_FUNC = new OrganizationCreateFuncImpl(
            TENANT_EXISTS_INPUT, TENANT_FIND_INPUT, ORGANIZATION_FIND_OUTPUT, ORGANIZATION_CREATE_OUTPUT, EVENT_PUBLISHER, CONTEXT_USER_HOLDER);

    @Test
    public void Organization_Create_Nominal() throws FunctionalException {
        Organization org = new Organization(null, TENANT_ID, "mylabel", "mycode", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        CompositeId compositeId = ORGANIZATION_CREATE_FUNC.create(org);
        assertNotNull(compositeId, "Composite id not null");
    }

    @Test
    public void Organization_No_Label_Validation_Error() throws FunctionalException {
        Organization org = new Organization(null, TENANT_ID, null, "mycode", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        ValidationException validationException = assertThrows(ValidationException.class, () -> ORGANIZATION_CREATE_FUNC.create(org));
        boolean notNullError = validationException.getValidationErrors().stream().anyMatch(ve -> "label".equals(ve.getFieldName())
                && ValidationRule.NOT_NULL.name().equals(ve.getValidationRule())
        );
        assertTrue(notNullError);
    }

    @Test
    public void Organization_Label_Too_Long_Validation_Error() throws FunctionalException {
        Organization org = new Organization(null, TENANT_ID, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras ac vulputate orci. Duis eget nisi faucibus, suscipit risus vel, iaculis purus.",
                "mycode", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        assertThrows(ValidationException.class, () -> ORGANIZATION_CREATE_FUNC.create(org));
    }

    @Test
    public void Tenant_Not_Found() throws FunctionalException {
        CompositeId secondTenant = new CompositeId(2L, UUID.randomUUID().toString());
        Organization org = new Organization(null, secondTenant, "mylabel", "mycode", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        FunctionalException functionalException = assertThrows(FunctionalException.class, () -> ORGANIZATION_CREATE_FUNC.create(org));
        assertEquals(FunctionalErrors.NOT_FOUND.name(), functionalException.getCode(), FunctionalErrors.NOT_FOUND.name() + " code ");
        assertEquals(Scope.TENANT.name(), functionalException.getScope(), Scope.TENANT.name() + " scope");
    }

    @Test
    public void Organization_Code_Already_Exist() throws FunctionalException {
        Organization organization = ((OrganizationFindOutputStub) ORGANIZATION_FIND_OUTPUT).orgsStub().get(0);
        FunctionalException functionalException = assertThrows(FunctionalException.class, () -> ORGANIZATION_CREATE_FUNC.create(organization));
        assertEquals(FunctionalErrors.ALREADY_EXISTS.name(), functionalException.getCode(), FunctionalErrors.ALREADY_EXISTS.name() + " code ");
    }

}