package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.functions.stubs.tenants.TenantExistsOutputStub;
import com.acme.jga.domain.functions.stubs.tenants.TenantUpdateOutputStub;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.domain.output.functions.tenants.TenantUpdateOutput;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TenantUpdateFuncImplTest {

    private static final String UID = UUID.randomUUID().toString();
    private static final Tenant TENANT = new Tenant(() -> UID, "tcode", "tlabel", TenantStatus.ACTIVE);
    private static final List<Tenant> TENANTS = List.of(TENANT);
    private static final TenantExistsOutputStub TENANT_EXISTS_OUTPUT_STUB = new TenantExistsOutputStub(TENANTS);
    private static final TenantExistsFuncImpl TENANT_EXISTS_FUNC = new TenantExistsFuncImpl(TENANT_EXISTS_OUTPUT_STUB);
    private static final TenantUpdateOutput TENANT_UPDATE_OUTPUT = new TenantUpdateOutputStub(TENANTS);
    private static final TenantUpdateFuncImpl TENANT_UPDATE_FUNC = new TenantUpdateFuncImpl(TENANT_UPDATE_OUTPUT, TENANT_EXISTS_FUNC);

    @Test
    void Tenant_Update_Nominal() throws FunctionalException {
        boolean updated = TENANT_UPDATE_FUNC.update(TENANT);
        assertTrue(updated);
    }

    @Test
    void Tenant_Update_Not_Found() {
        Tenant t = new Tenant(() -> UUID.randomUUID().toString(), "tcode", "tlabel", TenantStatus.ACTIVE);
        assertThrows(FunctionalException.class, () -> TENANT_UPDATE_FUNC.update(t));
    }
}