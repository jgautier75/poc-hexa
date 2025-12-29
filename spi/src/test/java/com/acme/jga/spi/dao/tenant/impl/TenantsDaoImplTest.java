package com.acme.jga.spi.dao.tenant.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.spi.dao.config.DatabaseTestConfig;
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

import java.awt.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
class TenantsDaoImplTest {
    private static String TENANT_CODE = "tenant_code_1";
    private static String TENANT_NAME = "tenant_name_1";

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
    private TenantsDao tenantsDao;

    @BeforeEach
    public void beforeTests() throws Exception {
        DaoTestUtils.performLiquibaseUpdate(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    }

    @Test
    public void Tenant_Operations_Nominal() throws FunctionalException {
        Tenant rdbmsTenantCreate = new Tenant(new CompositeId(null, UUID.randomUUID().toString()), TENANT_CODE, TENANT_NAME, TenantStatus.ACTIVE);
        CompositeId compositeId = tenantsDao.save(rdbmsTenantCreate);
        assertNotNull(compositeId, "CompositeId is null");

        boolean existsByExternalId = tenantsDao.existsByExternalId(compositeId.externalId(),null);
        assertTrue(existsByExternalId, "Tenant exists by externalId");

        boolean existsByCode = tenantsDao.existsByCode(TENANT_CODE);
        assertTrue(existsByCode, "Tenant exists by code");

        Tenant rdbmsTenantByCode = tenantsDao.findByCode(TENANT_CODE);
        assertNotNull(rdbmsTenantByCode, "RdbmsTenant is null");
        assertAll(() -> {
            assertEquals(TENANT_CODE, rdbmsTenantByCode.code(), "Tenant code match");
            assertEquals(TENANT_NAME, rdbmsTenantByCode.label(), "Tenant label match");
            assertEquals(TenantStatus.ACTIVE, rdbmsTenantByCode.status(), "Tenant status match");
        });

        Tenant rdbmsTenantById = tenantsDao.findByExternalId(rdbmsTenantByCode.id().externalId());
        assertNotNull(rdbmsTenantById, "RdbmsTenant is null");
        assertAll(() -> {
            assertEquals(TENANT_CODE, rdbmsTenantById.code(), "Tenant code match");
            assertEquals(TENANT_NAME, rdbmsTenantById.label(), "Tenant label match");
            assertEquals(TenantStatus.ACTIVE, rdbmsTenantById.status(), "Tenant status match");
        });

        List<Tenant> allTenants = tenantsDao.findAll();
        assertAll(() -> {
            assertNotNull(allTenants, "AllTenants is null");
            assertFalse(allTenants.isEmpty(), "AllTenants is not empty");
        });

        String updatedCode = "tenant_code_2";
        String updatedLabel = "tenant_name_2";
        Tenant updatedRdbmsTenant = new Tenant(rdbmsTenantByCode.id(), updatedCode, updatedLabel, TenantStatus.INACTIVE);
        boolean updated = tenantsDao.update(updatedRdbmsTenant);
        assertTrue(updated, "Tenant updated");

        Tenant rdbmsUpdatedByCode = tenantsDao.findByCode(updatedCode);
        assertAll(() -> {
            assertNotNull(rdbmsUpdatedByCode, "RdbmsTenant is not null");
            assertEquals(updatedCode, rdbmsUpdatedByCode.code(), "Tenant code match");
            assertEquals(updatedLabel, rdbmsUpdatedByCode.label(), "Tenant code match");
        });

        tenantsDao.delete(rdbmsUpdatedByCode.id());
        boolean exists = tenantsDao.existsByExternalId(rdbmsUpdatedByCode.id().externalId(),null);
        assertFalse(exists, "Tenant does not exists by externalId (deleted)");
    }

}