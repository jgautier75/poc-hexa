package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.functions.stubs.tenants.TenantExistsOutputStub;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TenantExistsFuncImplTest {

    private static final String UID = UUID.randomUUID().toString();
    private static final Tenant TENANT = new Tenant(() -> UID, "tcode", "tlabel", TenantStatus.ACTIVE);
    private static final List<Tenant> TENANTS = List.of(TENANT);
    private static final TenantExistsOutputStub TENANT_EXISTS_OUTPUT_STUB = new TenantExistsOutputStub(TENANTS);
    private static final TenantExistsFuncImpl TENANT_EXISTS_FUNC = new TenantExistsFuncImpl(TENANT_EXISTS_OUTPUT_STUB);

    @Test
    void Tenant_Exists_By_Code_Nominal() {
        boolean exists = TENANT_EXISTS_FUNC.existsByCode(TENANT.code());
        assertTrue(exists, "Tenant exists");
    }

    @Test
    void Tenant_Exists_By_External_Id_Nominal() {
        boolean exists = TENANT_EXISTS_FUNC.existsByExternalId(TENANT.tenantId().get());
        assertTrue(exists, "Tenant exists");
    }
}