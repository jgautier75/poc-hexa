package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.functions.stubs.tenants.TenantDeleteOuputStub;
import com.acme.jga.domain.functions.stubs.tenants.TenantExistsFuncStub;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TenantDeleteFuncImplTest {

    private static final String UID = java.util.UUID.randomUUID().toString();
    private static final Tenant TENANT = new Tenant(() -> UID, "tcode", "tlabel", TenantStatus.ACTIVE);
    private static final List<Tenant> TENANTS = List.of(TENANT);
    private static final TenantExistsFuncStub TENANT_EXISTS_FUNC_STUB = new TenantExistsFuncStub(TENANTS);
    private static final CompositeId COMPOSITE_ID = new CompositeId(IdKind.BOTH, 1L, TENANT.tenantId().get());
    private static final TenantDeleteOuputStub DELETE_OUPUT_STUB = new TenantDeleteOuputStub(List.of(COMPOSITE_ID));
    private static final TenantDeleteFuncImpl TENANT_DELETE_FUNC_IMPL = new TenantDeleteFuncImpl(TENANT_EXISTS_FUNC_STUB, DELETE_OUPUT_STUB);

    @Test
    void Tenant_Delete_Nominal() throws FunctionalException {
        boolean deleted = TENANT_DELETE_FUNC_IMPL.deleteTenant(TENANT.tenantId());
        assertTrue(deleted, "Tenant deleted");
    }

    @Test
    void Tenant_Delete_Not_Exist() {
        assertThrows(FunctionalException.class, () -> TENANT_DELETE_FUNC_IMPL.deleteTenant(() -> java.util.UUID.randomUUID().toString()));
    }

}