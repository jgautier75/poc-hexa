package com.acme.jga.domain.functions.tenants.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.functions.stubs.tenants.TenantExistsFuncStub;
import com.acme.jga.domain.functions.stubs.tenants.TenantFindOutputStub;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import io.opentelemetry.api.trace.TracerProvider;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TenantFindFuncImplTest {
    private static final String UID = java.util.UUID.randomUUID().toString();
    private static final CompositeId TENANT_ID = new CompositeId(1L, UID);
    private static final Tenant TENANT = new Tenant(TENANT_ID, "tcode", "tlabel", TenantStatus.ACTIVE);
    private static final List<Tenant> TENANTS = List.of(TENANT);
    private static final TenantFindOutputStub TENANT_FIND_OUTPUT_STUB = new TenantFindOutputStub(TENANTS);
    private static final TenantExistsFuncStub TENANT_EXISTS_FUNC_STUB = new TenantExistsFuncStub(TENANTS);
    private static final TenantFindFuncImpl TENANT_FIND_FUNC = new TenantFindFuncImpl(TENANT_FIND_OUTPUT_STUB, TENANT_EXISTS_FUNC_STUB, TracerProvider.noop());

    @Test
    void Tenant_Find_By_Code_Nominal() throws FunctionalException {
        Tenant byCode = TENANT_FIND_FUNC.findByCode(TENANT.code());
        assertNotNull(byCode, "Tenant by code found");
    }

    @Test
    void Tenant_Find_By_Code_Not_Found() {
        assertThrows(FunctionalException.class, () -> TENANT_FIND_FUNC.findByCode("la_tete_a_toto"));
    }

    @Test
    void Tenant_Find_By_Id_Nominal() throws FunctionalException {
        Tenant tenant = TENANT_FIND_FUNC.findById(TENANT.id(), null);
        assertNotNull(tenant, "Tenant by externalId found");
    }

    @Test
    void Tenant_Find_By_Id_Not_Found() throws FunctionalException {
        CompositeId testId = new CompositeId(2L, UUID.randomUUID().toString());
        assertThrows(FunctionalException.class, () -> TENANT_FIND_FUNC.findById(testId, null));
    }
}