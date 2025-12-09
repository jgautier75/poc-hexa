package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.functions.stubs.tenants.TenantCreateOutputStub;
import com.acme.jga.domain.functions.stubs.tenants.TenantExistsFuncStub;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantId;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.domain.output.functions.tenants.TenantCreateOutput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

class TenantCreateFuncImplTest {

    private static final Tenant TENANT = new Tenant(() -> UUID.randomUUID().toString(), "tcode", "tlabel", TenantStatus.ACTIVE);
    private static final List<Tenant> TENANTS = List.of(TENANT);
    private static final TenantExistsFuncStub TENANT_EXISTS_FUNC_STUB = new TenantExistsFuncStub(TENANTS);
    private static final TenantCreateOutput TENANT_CREATE_OUTPUT_STUB = new TenantCreateOutputStub();
    private static final TenantCreateFuncImpl TENANT_CREATE_FUNC_IMPL = new TenantCreateFuncImpl(TENANT_EXISTS_FUNC_STUB, TENANT_CREATE_OUTPUT_STUB);

    @Test
    void Tenant_Create_Nominal() throws FunctionalException {
        Tenant t = new Tenant(() -> UUID.randomUUID().toString(), "test", "tlabel", TenantStatus.ACTIVE);
        TenantId tenantId = TENANT_CREATE_FUNC_IMPL.create(t);
        assertNotNull(tenantId);
    }

    @Test
    void Tenant_Create_Code_Already_Exists() {
        assertThrows(FunctionalException.class, () -> TENANT_CREATE_FUNC_IMPL.create(TENANT));
    }
}