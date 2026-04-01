package com.acme.jga.spi.dao.utils;

import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import org.springframework.test.context.DynamicPropertyRegistry;

import java.sql.Connection;
import java.sql.DriverManager;

public class DaoTestUtils {
    public static final String POSTGRESQL_VERSION = "postgres:18.3";
    public static final String SPRING_DS_URL = "spring.datasource.url";
    public static final String SPRING_DS_USER = "spring.datasource.username";
    public static final String SPRING_DS_PASS = "spring.datasource.password";

    public static void performLiquibaseUpdate(String jdbcUrl, String userName, String userPassword) throws Exception {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, userPassword)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conn));
            CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
            updateCommand.addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, database);
            updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, "postgresql/changelogs.xml");
            updateCommand.execute();
        }
    }

    public static void registerPgDatasource(DynamicPropertyRegistry registry, String url, String user, String password) throws Exception {
        registry.add(DaoTestUtils.SPRING_DS_URL, () -> url);
        registry.add(DaoTestUtils.SPRING_DS_USER, () -> user);
        registry.add(DaoTestUtils.SPRING_DS_PASS, () -> password);
    }

}
