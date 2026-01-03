package com.acme.jga.spi.dao.users.impl;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.model.user.UserStatus;
import com.acme.jga.search.filtering.expr.Expression;
import com.acme.jga.spi.dao.config.DatabaseTestConfig;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import com.acme.jga.spi.dao.processors.ExpressionsProcessor;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import com.acme.jga.spi.dao.users.api.UsersDao;
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

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
class UsersDaoImplTest {

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
    UsersDao usersDao;

    @BeforeEach
    public void beforeTests() throws Exception {
        DaoTestUtils.performLiquibaseUpdate(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    }

    @Test
    public void User_Operations_Nominal() {

        // Create tenant
        Tenant tenant = new Tenant(null, "tenant-code", "tenant-label", TenantStatus.ACTIVE);
        CompositeId tenantId = tenantsDao.save(tenant);
        assertNotNull(tenantId, "Tenant not null");

        // Create organization
        Organization org = new Organization(null, tenantId, "org-label", "org-code", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        CompositeId orgId = organizationsDao.save(org);
        assertNotNull(orgId, "Organization not null");

        // Create user
        User user = new User(null, tenantId, orgId, "toto", "tata", "tutu", "w",
                "tata.tutu@test.fr", UserStatus.ACTIVE, "notif@test.fr", "123456");
        CompositeId userId = usersDao.create(user);
        assertNotNull(userId, "User not null");

        // User exists by email
        boolean emailExists = usersDao.emailExists(user.email());
        assertTrue(emailExists, "Email already used");

        // User exists by login
        boolean loginExists = usersDao.loginExists(user.login());
        assertTrue(loginExists, "Login already exists");

        // Find user by id
        User userById = usersDao.findById(userId, tenantId, orgId);
        assertAll(
                () -> assertNotNull(userById, "Found user by id"),
                () -> assertEquals(user.email(), userById.email(), "Email match"),
                () -> assertEquals(user.firstName(), userById.firstName(), "First name match"),
                () -> assertEquals(user.lastName(), userById.lastName(), "Last name match"),
                () -> assertEquals(user.notifEmail(), userById.notifEmail(), "Notif email match"),
                () -> assertEquals(user.middleName(), userById.middleName(), "Middle name match"),
                () -> assertEquals(user.login(), userById.login(), "Login match"),
                () -> assertEquals(user.status(), userById.status(), "Status match")
        );

        // List users
        List<User> allUsers = usersDao.findAll(tenantId, orgId, new HashMap<>());
        assertEquals(1, allUsers.size(), "Found 1 user");

        // Update user
        User upUser = new User(userById.id(), userById.tenantId(), userById.organizationId(), "zaza", "zozo", "zuzu",
                "z", "za@test.fr", UserStatus.INACTIVE, "unotif@test.fr", null);
        Integer update = usersDao.update(upUser);
        User updatedUser = usersDao.findById(userById.id(), userById.tenantId(), userById.organizationId());
        assertAll(
                () -> assertNotNull(updatedUser, "Updated user not null"),
                () -> assertEquals(upUser.status(), updatedUser.status(), "Status match"),
                () -> assertEquals(upUser.lastName(), updatedUser.lastName(), "Last name match"),
                () -> assertEquals(upUser.firstName(), updatedUser.firstName(), "First name match"),
                () -> assertEquals(upUser.middleName(), updatedUser.middleName(), "Middle name match"),
                () -> assertEquals(upUser.email(), updatedUser.email(), "Email match"),
                () -> assertEquals(upUser.notifEmail(), updatedUser.notifEmail(), "Notif email match")
        );

        Integer nbDeleted = usersDao.deleteUser(userById.id(), userById.tenantId(), userById.organizationId());
        User deletedUser = usersDao.findById(userId, tenantId, orgId);
        assertNull(deletedUser, "User is deleted");
    }

}