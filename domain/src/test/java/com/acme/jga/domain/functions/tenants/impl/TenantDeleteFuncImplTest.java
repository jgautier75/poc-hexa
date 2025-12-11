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
    private static final CompositeId TENANT_ID = new CompositeId(1L, UID);
    private static final Tenant TENANT = new Tenant(TENANT_ID, "tcode", "tlabel", TenantStatus.ACTIVE);
    private static final List<Tenant> TENANTS = List.of(TENANT);
    private static final TenantExistsFuncStub TENANT_EXISTS_FUNC_STUB = new TenantExistsFuncStub(TENANTS);
    private static final TenantDeleteOuputStub DELETE_OUPUT_STUB = new TenantDeleteOuputStub(List.of(TENANT_ID));
    private static final TenantDeleteFuncImpl TENANT_DELETE_FUNC_IMPL = new TenantDeleteFuncImpl(TENANT_EXISTS_FUNC_STUB, DELETE_OUPUT_STUB);

    @Test
    void Tenant_Delete_Nominal() throws FunctionalException {
        boolean deleted = TENANT_DELETE_FUNC_IMPL.deleteTenant(TENANT.id());
        assertTrue(deleted, "Tenant deleted");
    }

    @Test
    void Tenant_Delete_Not_Exist() {
        assertThrows(FunctionalException.class, () -> TENANT_DELETE_FUNC_IMPL.deleteTenant(() -> java.util.UUID.randomUUID().toString()));
    }

}