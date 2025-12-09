package com.acme.jga.spi.dao.tenant.impl;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.spi.dao.config.DatabaseTestConfig;
import com.acme.jga.spi.dao.tenant.api.TenantsDao;
import com.acme.jga.spi.dao.utils.DaoTestUtils;
import com.acme.jga.spi.jdbc.model.RdbmsTenant;
import com.acme.jga.spi.jdbc.model.RdbmsTenantCreate;
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
class TenantsDaoImplTest {

    private static String tenantCode = "tenant_code_1";
    private static String tenantName = "tenant_name_1";

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
    public void Tenant_Operations_Nominal() {

        RdbmsTenantCreate rdbmsTenantCreate = new RdbmsTenantCreate(tenantCode, tenantName, TenantStatus.ACTIVE);
        CompositeId compositeId = tenantsDao.save(rdbmsTenantCreate);
        assertNotNull(compositeId, "CompositeId is null");

        boolean existsByExternalId = tenantsDao.existsByExternalId(compositeId.externalId());
        assertTrue(existsByExternalId, "Tenant exists by externalId");

        boolean existsByCode = tenantsDao.existsByCode(tenantCode);
        assertTrue(existsByCode, "Tenant exists by code");

        RdbmsTenant rdbmsTenantByCode = tenantsDao.findByCode(tenantCode);
        assertNotNull(rdbmsTenantByCode, "RdbmsTenant is null");
        assertAll(() -> {
            assertEquals(tenantCode, rdbmsTenantByCode.code(), "Tenant code match");
            assertEquals(tenantName, rdbmsTenantByCode.label(), "Tenant label match");
            assertEquals(TenantStatus.ACTIVE, rdbmsTenantByCode.tenantStatus(), "Tenant status match");
        });


        RdbmsTenant rdbmsTenantById = tenantsDao.findByExternalId(rdbmsTenantByCode.compositeId().externalId());
        assertNotNull(rdbmsTenantById, "RdbmsTenant is null");
        assertAll(() -> {
            assertEquals(tenantCode, rdbmsTenantById.code(), "Tenant code match");
            assertEquals(tenantName, rdbmsTenantById.label(), "Tenant label match");
            assertEquals(TenantStatus.ACTIVE, rdbmsTenantById.tenantStatus(), "Tenant status match");
        });

        List<RdbmsTenant> allTenants = tenantsDao.findAll();
        assertAll(() -> {
            assertNotNull(allTenants, "AllTenants is null");
            assertFalse(allTenants.isEmpty(), "AllTenants is not empty");
        });


        String updatedCode = "tenant_code_2";
        String updatedLabel = "tenant_name_2";
        RdbmsTenant updatedRdbmsTenant = new RdbmsTenant(rdbmsTenantByCode.compositeId(), updatedCode, updatedLabel, TenantStatus.INACTIVE);
        boolean updated = tenantsDao.update(updatedRdbmsTenant);
        assertTrue(updated, "Tenant updated");


        RdbmsTenant rdbmsUpdatedByCode = tenantsDao.findByCode(updatedCode);
        assertAll(() -> {
            assertNotNull(rdbmsUpdatedByCode, "RdbmsTenant is not null");
            assertEquals(updatedCode, rdbmsUpdatedByCode.code(), "Tenant code match");
            assertEquals(updatedLabel, rdbmsUpdatedByCode.label(), "Tenant code match");
        });

        tenantsDao.delete(rdbmsUpdatedByCode.compositeId());
        boolean exists = tenantsDao.existsByExternalId(rdbmsUpdatedByCode.compositeId().externalId());
        assertFalse(exists, "Tenant does not exists by externalId (deleted)");

    }

}