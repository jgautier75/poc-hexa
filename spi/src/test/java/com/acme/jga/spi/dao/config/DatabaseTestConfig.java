package com.acme.jga.spi.dao.config;

import com.acme.jga.spi.dao.organizations.impl.OrganizationsDaoImpl;
import com.acme.jga.spi.dao.sectors.impl.SectorsDaoImpl;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import com.acme.jga.spi.dao.tenants.impl.TenantsDaoImpl;
import io.micrometer.observation.ObservationRegistry;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DatabaseTestConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Autowired DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TenantsDao tenantsDao(@Autowired ObservationRegistry observationRegistry,
                                 @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                 @Autowired SdkLoggerProvider sdkLoggerProvider) {
        return new TenantsDaoImpl(observationRegistry, namedParameterJdbcTemplate, sdkLoggerProvider);
    }

    @Bean
    public OrganizationsDaoImpl organizationsDao(@Autowired ObservationRegistry observationRegistry,
                                                 @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                 @Autowired SdkLoggerProvider sdkLoggerProvider) {
        return new OrganizationsDaoImpl(observationRegistry, namedParameterJdbcTemplate, sdkLoggerProvider);
    }

    @Bean
    public SectorsDaoImpl sectorsDao(@Autowired ObservationRegistry observationRegistry,
                                     @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                     @Autowired SdkLoggerProvider sdkLoggerProvider) {
        return new SectorsDaoImpl(observationRegistry, namedParameterJdbcTemplate, sdkLoggerProvider);
    }

    @Bean
    public ObservationRegistry observationRegistry() {
        return ObservationRegistry.NOOP;
    }

    @Bean
    public SdkLoggerProvider sdkLoggerProvider() {
        return SdkLoggerProvider.builder().build();
    }

}
