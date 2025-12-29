package com.acme.jga.spi.dao.organizations.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.spi.dao.config.DatabaseTestConfig;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
class OrganizationsDaoImplTest {

    private static final String ORG_CODE = "myorg";
    private static final String ORG_LABEL = "My Org";
    private static final String ORG_COUNTRY = "fr";

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

    @BeforeEach
    public void beforeTests() throws Exception {
        DaoTestUtils.performLiquibaseUpdate(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    }

    @Test
    public void Organization_Operations_Nominal() throws FunctionalException {
        Tenant tenant = new Tenant(null, "mytenant", "My Tenant", TenantStatus.ACTIVE);
        CompositeId tenantId = tenantsDao.save(tenant);
        assertNotNull(tenantId, "Tenant created");

        Organization organization = new Organization(null, tenantId, ORG_LABEL, ORG_CODE, OrganizationKind.COMMUNITY, ORG_COUNTRY, OrganizationStatus.ACTIVE);
        CompositeId orgId = organizationsDao.save(organization);
        assertNotNull(orgId, "Organization created");

        Organization orgFound = organizationsDao.findByTenantAndId(tenantId, orgId);
        assertNotNull(orgFound, "Organization found");

        boolean exists = organizationsDao.existsByCode(ORG_CODE);
        assertTrue(exists, "Organization exists by code");

        List<Organization> orgs = organizationsDao.findAll(tenantId, null);
        assertEquals(1, orgs.size(), "Organizations found");

        String updatedName = "My Org Label";
        String updatedCode = "myorgup";
        String updatedCountry = "de";
        OrganizationStatus updatedStatus = OrganizationStatus.INACTIVE;

        organizationsDao.update(tenantId, orgFound.id(), updatedCode, updatedName, updatedCountry, updatedStatus);

        Organization updatedOrg = organizationsDao.findByTenantAndId(tenantId, orgId);
        assertAll(
                () -> {
                    assertNotNull(updatedOrg, "Organization updated");
                    assertEquals(updatedName, updatedOrg.label(), "Organization updated label match");
                    assertEquals(updatedCode, updatedOrg.code(), "Organization updated code match");
                    assertEquals(updatedCountry, updatedOrg.country(), "Organization updated country match");
                    assertEquals(updatedStatus, updatedOrg.status(), "Organization updated status match");
                }
        );

        organizationsDao.delete(tenantId, orgId);

        Organization byTenantAndId = organizationsDao.findByTenantAndId(tenantId, orgId);
        assertNull(byTenantAndId, "Organization deleted by code");

    }
}