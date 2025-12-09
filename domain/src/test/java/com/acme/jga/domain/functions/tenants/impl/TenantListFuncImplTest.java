package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.functions.stubs.tenants.TenantListOutputStub;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TenantListFuncImplTest {

    private static final Tenant TENANT = new Tenant(() -> UUID.randomUUID().toString(), "tcode", "tlabel", TenantStatus.ACTIVE);
    private static final List<Tenant> TENANTS = List.of(TENANT);
    private static final TenantListOutputStub TENANT_LIST_OUTPUT_STUB = new TenantListOutputStub(TENANTS);
    private static final TenantListFuncImpl TENANT_LIST_FUNC = new TenantListFuncImpl(TENANT_LIST_OUTPUT_STUB);

    @Test
    void list() {
        List<Tenant> tenants = TENANT_LIST_FUNC.list();
        assertNotNull(tenants, "Tenants list not null");
    }
}