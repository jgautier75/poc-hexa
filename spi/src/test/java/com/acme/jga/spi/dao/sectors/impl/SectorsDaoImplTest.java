package com.acme.jga.spi.dao.sectors.impl;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.spi.dao.config.DatabaseTestConfig;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import com.acme.jga.spi.dao.sectors.api.SectorsDao;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import com.acme.jga.spi.dao.utils.DaoTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
class SectorsDaoImplTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DaoTestUtils.POSTGRESQL_VERSION);

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    TenantsDao tenantsDao;

    @Autowired
    OrganizationsDao organizationsDao;

    @Autowired
    SectorsDao sectorsDao;

    @BeforeEach
    public void beforeTests() throws Exception {
        DaoTestUtils.performLiquibaseUpdate(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    }

    @Test
    public void Sector_Operations_Nominal() {
        Tenant tenant = new Tenant(null, "mytenantcode", "mytenantlabel", TenantStatus.ACTIVE);
        CompositeId tenantId = tenantsDao.save(tenant);
        assertNotNull(tenantId, "Tenant created");

        Organization organization = new Organization(null, tenantId, "myorganizationcode", "myorganizationlabel", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        CompositeId organizationId = organizationsDao.save(organization);
        assertNotNull(organizationId, "Organization created");

        Sector sector = new Sector(null, tenantId, organizationId, "mysectorlabel", "mysectorcode", null, true, null);
        CompositeId sectorId = sectorsDao.save(sector);
        assertNotNull(sectorId, "Sector created");

        Sector sectorById = sectorsDao.findById(tenantId, organizationId, sectorId);
        assertNotNull(sectorById, "Sector found");

        Sector sectorUpdate = new Sector(sectorId, tenantId, organizationId, "myuplabel", "myupcode", null, true, null);
        Integer update = sectorsDao.update(sectorUpdate);
        assertNotNull(update, "Sector updated");

        Sector childSector = new Sector(null, tenantId, organizationId, "mychildsectorcode", "mychildsectorlabel", sectorId, false, null);
        CompositeId childSectorId = sectorsDao.save(childSector);
        assertNotNull(childSectorId, "Child sector found");

        List<Sector> allSectors = sectorsDao.findAll(tenantId, organizationId);
        assertNotNull(allSectors, "Sectors found");
        assertEquals(allSectors.size(), 2, "Sectors found");
    }

}