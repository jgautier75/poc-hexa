package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.functions.stubs.tenants.TenantCreateOutputStub;
import com.acme.jga.domain.functions.stubs.tenants.TenantExistsFuncStub;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.domain.output.functions.tenants.TenantCreateOutput;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TenantCreateFuncImplTest {
    private static final String UID = java.util.UUID.randomUUID().toString();
    private static final CompositeId TENANT_ID = new CompositeId(1L, UID);
    private static final Tenant TENANT = new Tenant(TENANT_ID, "tcode", "tlabel", TenantStatus.ACTIVE);
    private static final List<Tenant> TENANTS = List.of(TENANT);
    private static final TenantExistsFuncStub TENANT_EXISTS_FUNC_STUB = new TenantExistsFuncStub(TENANTS);
    private static final TenantCreateOutput TENANT_CREATE_OUTPUT_STUB = new TenantCreateOutputStub();
    private static final TenantCreateFuncImpl TENANT_CREATE_FUNC_IMPL = new TenantCreateFuncImpl(TENANT_EXISTS_FUNC_STUB, TENANT_CREATE_OUTPUT_STUB);

    @Test
    void Tenant_Create_Nominal() throws FunctionalException {
        Tenant t = new Tenant(null, "test", "tlabel", TenantStatus.ACTIVE);
        CompositeId tenantId = TENANT_CREATE_FUNC_IMPL.create(t);
        assertNotNull(tenantId);
    }

    @Test
    void Tenant_Create_Code_Already_Exists() {
        assertThrows(FunctionalException.class, () -> TENANT_CREATE_FUNC_IMPL.create(TENANT));
    }
}