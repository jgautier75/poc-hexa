package com.acme.jga.domain.functions.sectors.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.functions.stubs.organizations.OrganizationFindInputStub;
import com.acme.jga.domain.functions.stubs.sectors.SectorFindOuputStub;
import com.acme.jga.domain.functions.stubs.tenants.TenantFindInputStub;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.domain.output.functions.sectors.SectorFindOuput;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SectorHierarchyFuncImplTest {

    private static final CompositeId TENANT_ID = new CompositeId(1L, UUID.randomUUID().toString());
    private static final Tenant TENANT = new Tenant(TENANT_ID, "root-tenant", "root-tenant", TenantStatus.ACTIVE);
    private static final TenantFindInput TENANT_FIND_INPUT = new TenantFindInputStub(TENANT);
    private static final OrganizationFindInput ORGANIZATION_FIND_INPUT = new OrganizationFindInputStub(TENANT);
    private static final SectorFindOuput SECTOR_FIND_OUPUT_STUB = new SectorFindOuputStub(TENANT, ((OrganizationFindInputStub) ORGANIZATION_FIND_INPUT).getOrganization());

    @Test
    void Sector_Hierarchy_Nominal() throws FunctionalException {
        SectorHierarchyFuncImpl sectorHierarchyFunc = new SectorHierarchyFuncImpl(TENANT_FIND_INPUT, ORGANIZATION_FIND_INPUT, SECTOR_FIND_OUPUT_STUB);
        Sector sectorHierarchy = sectorHierarchyFunc.findSectorHierarchy(TENANT_ID, ((OrganizationFindInputStub) ORGANIZATION_FIND_INPUT).getOrganization().id());
        assertAll("Hierarchy checks", () -> {
            assertNotNull(sectorHierarchy, "Sector hierarchy not null");
            assertEquals(4, sectorHierarchy.getChildren().size(), "4 children under root");
            assertTrue(sectorHierarchy.getChildren().stream().anyMatch(child -> child.getCode().equals("north")));
            assertTrue(sectorHierarchy.getChildren().stream().anyMatch(child -> child.getCode().equals("south")));
            assertTrue(sectorHierarchy.getChildren().stream().anyMatch(child -> child.getCode().equals("east")));
            assertTrue(sectorHierarchy.getChildren().stream().anyMatch(child -> child.getCode().equals("west")));
        });
        Sector northSector = sectorHierarchy.getChildren().stream().filter(child -> child.getCode().equals("north")).findFirst().get();
        assertAll("North Sector checks", () -> {
            assertTrue(northSector.getChildren().stream().anyMatch(child -> child.getCode().equals("northeast")));
            assertTrue(northSector.getChildren().stream().anyMatch(child -> child.getCode().equals("northwest")));
        });
        Sector northEastSector = northSector.getChildren().stream().filter(child -> child.getCode().equals("northeast")).findFirst().get();
        assertAll("North East Sector checks", () -> {
            assertEquals(1, northEastSector.getChildren().size(), "North East Sector has 2 children");
            assertTrue(northEastSector.getChildren().stream().anyMatch(child -> child.getCode().equals("northnortheast")));
        });
    }
}