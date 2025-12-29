package com.acme.jga.spi.dao.config;

import com.acme.jga.spi.dao.organizations.impl.OrganizationsDaoImpl;
import com.acme.jga.spi.dao.sectors.impl.SectorsDaoImpl;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import com.acme.jga.spi.dao.tenants.impl.TenantsDaoImpl;
import io.opentelemetry.api.trace.TracerProvider;
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
    public TenantsDao tenantsDao(@Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate, @Autowired TracerProvider tracerProvider) {
        return new TenantsDaoImpl(tracerProvider, namedParameterJdbcTemplate);
    }

    @Bean
    public OrganizationsDaoImpl organizationsDao(@Autowired TracerProvider tracerProvider, @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new OrganizationsDaoImpl(tracerProvider, namedParameterJdbcTemplate);
    }

    @Bean
    public SectorsDaoImpl sectorsDao(@Autowired TracerProvider tracerProvider, @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new SectorsDaoImpl(tracerProvider, namedParameterJdbcTemplate);
    }

    @Bean
    public TracerProvider tracerProvider() {
        return TracerProvider.noop();
    }

}
