package com.acme.jga.spi.dao.config;

import com.acme.jga.spi.dao.organizations.impl.OrganizationsDaoImpl;
import com.acme.jga.spi.dao.processors.ExpressionsProcessor;
import com.acme.jga.spi.dao.processors.FilterSqlBuilder;
import com.acme.jga.spi.dao.processors.PaginationBuilder;
import com.acme.jga.spi.dao.processors.PropertyMetadataResolver;
import com.acme.jga.spi.dao.sectors.impl.SectorsDaoImpl;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import com.acme.jga.spi.dao.tenants.impl.TenantsDaoImpl;
import com.acme.jga.spi.dao.users.impl.UsersDaoImpl;
import io.micrometer.observation.ObservationRegistry;
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
    public TenantsDao tenantsDao(@Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new TenantsDaoImpl(namedParameterJdbcTemplate);
    }

    @Bean
    public OrganizationsDaoImpl organizationsDao(@Autowired DataSource dataSource,
                                                 @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                 @Autowired ExpressionsProcessor expressionsProcessor) {
        return new OrganizationsDaoImpl(dataSource, namedParameterJdbcTemplate, expressionsProcessor);
    }

    @Bean
    public SectorsDaoImpl sectorsDao(@Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new SectorsDaoImpl(namedParameterJdbcTemplate);
    }

    @Bean
    public PaginationBuilder paginationBuilder() {
        return new PaginationBuilder();
    }

    @Bean
    public PropertyMetadataResolver propertyMetadataResolver() {
        return new PropertyMetadataResolver();
    }

    @Bean
    public FilterSqlBuilder filterSqlBuilder(@Autowired PropertyMetadataResolver propertyMetadataResolver) {
        return new FilterSqlBuilder(propertyMetadataResolver);
    }

    @Bean
    public ExpressionsProcessor expressionsHandler(@Autowired FilterSqlBuilder filterSqlBuilder, @Autowired PaginationBuilder paginationBuilder) {
        return new ExpressionsProcessor(filterSqlBuilder, paginationBuilder);
    }

    @Bean
    public UsersDaoImpl usersDao(@Autowired DataSource dataSource,
                                 @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                 @Autowired ExpressionsProcessor expressionsProcessor) {
        return new UsersDaoImpl(dataSource, namedParameterJdbcTemplate, expressionsProcessor);
    }

    @Bean
    public ObservationRegistry observationRegistry() {
        return ObservationRegistry.NOOP;
    }

}
