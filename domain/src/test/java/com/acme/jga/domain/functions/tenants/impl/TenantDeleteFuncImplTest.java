package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.functions.stubs.tenants.TenantDeleteOutputStub;
import com.acme.jga.domain.functions.stubs.tenants.TenantFindInputStub;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.domain.output.functions.tenants.TenantDeleteOuput;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TenantDeleteFuncImplTest {
    private static final String UID = java.util.UUID.randomUUID().toString();
    private static final CompositeId TENANT_ID = new CompositeId(1L, UID);
    private static final Tenant TENANT = new Tenant(TENANT_ID, "tcode", "tlabel", TenantStatus.ACTIVE);
    private static final List<Tenant> TENANTS = List.of(TENANT);
    private static final TenantFindInput TENANT_FIND_INPUT = new TenantFindInputStub(TENANT);
    private static final TenantDeleteOuput TENANT_DELETE_OUTPUT = new TenantDeleteOutputStub();
    private static final TenantDeleteFuncImpl TENANT_DELETE_FUNC_IMPL = new TenantDeleteFuncImpl(TENANT_FIND_INPUT, TENANT_DELETE_OUTPUT);

    @Test
    void Tenant_Delete_Nominal() throws FunctionalException {
        boolean deleted = TENANT_DELETE_FUNC_IMPL.deleteTenant(TENANT.id());
        assertTrue(deleted, "Tenant deleted");
    }

}