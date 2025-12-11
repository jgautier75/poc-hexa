package com.acme.jga.spi.dao.organizations.impl;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import com.acme.jga.spi.dao.tenants.impl.TenantsDaoImpl;
import com.acme.jga.spi.jdbc.extractors.OrganizationExtractor;
import com.acme.jga.spi.jdbc.utils.AbstractJdbcDaoSupport;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import com.acme.jga.spi.jdbc.utils.WhereClause;
import com.acme.jga.spi.jdbc.utils.WhereOperator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class OrganizationsDaoImpl extends AbstractJdbcDaoSupport implements OrganizationsDao {

    public OrganizationsDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        super.loadQueryFilePath(TenantsDaoImpl.class.getClassLoader(), new String[]{"organizations.properties"});
    }

    @Override
    public CompositeId save(Organization org) {
        String baseQuery = super.getQuery("org_create");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String uuid = DaoConstants.generatedUUID();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(DaoConstants.P_UID, uuid);
        mapSqlParameterSource.addValue(DaoConstants.P_TENANT_ID, org.tenantId().internalId());
        mapSqlParameterSource.addValue(DaoConstants.P_CODE, org.code());
        mapSqlParameterSource.addValue(DaoConstants.P_LABEL, org.label());
        mapSqlParameterSource.addValue(DaoConstants.P_COUNTRY, org.country());
        mapSqlParameterSource.addValue(DaoConstants.P_KIND, org.kind().getValue());
        mapSqlParameterSource.addValue(DaoConstants.P_STATUS, org.status().getValue());
        super.getNamedParameterJdbcTemplate().update(baseQuery, mapSqlParameterSource, keyHolder);
        Long generatedId = super.extractGeneratedId(keyHolder, DaoConstants.FIELD_ID);
        return new CompositeId(generatedId, uuid);
    }

    @Override
    public Organization findByTenantAndId(CompositeId tenantId, CompositeId id) {
        String baseQuery = super.getQuery("org_sel_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        addWhereClauseForId(whereClauses, id);
        addWhereClauseForId(whereClauses, tenantId);
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return OrganizationExtractor.extractOrganization(rs, true, tenantId);
        });
    }

    @Override
    public Integer update(CompositeId tenantId, CompositeId orgId, String code, String label, String country, OrganizationStatus status) {
        String baseQuery = super.getQuery("org_update");
        List<WhereClause> whereClauses = new ArrayList<>();
        addWhereClauseForId(whereClauses, orgId);
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_TENANT_ID, DaoConstants.P_TENANT_ID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_TENANT_ID)
                .paramValue(tenantId.internalId()).build());
        Map<String, Object> params = super.buildParams(whereClauses);
        params.put(DaoConstants.P_CODE, code);
        params.put(DaoConstants.P_LABEL, label);
        params.put(DaoConstants.P_COUNTRY, country);
        params.put(DaoConstants.P_STATUS, status.getValue());
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().update(fullQuery, params);
    }

    @Override
    public Integer delete(CompositeId tenantId, CompositeId orgId) {
        String baseQuery = super.getQuery("org_delete");
        List<WhereClause> whereClauses = new ArrayList<>();
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        Map<String, Object> params = super.buildParams(whereClauses);
        params.put(DaoConstants.P_TENANT_ID, tenantId.internalId());
        params.put(DaoConstants.P_ID, orgId.internalId());
        return super.getNamedParameterJdbcTemplate().update(fullQuery, params);
    }

    @Override
    public boolean existsByCode(String code) {
        String baseQuery = super.getQuery("org_count");
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_CODE, DaoConstants.P_CODE))
                .paramName(DaoConstants.P_CODE)
                .operator(WhereOperator.AND)
                .paramValue(code)
                .build());
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        Map<String, Object> params = super.buildParams(whereClauses);
        return super.executeExists(fullQuery, params);
    }

    @Override
    public List<Organization> findAll(CompositeId tenantId) {
        String baseQuery = super.getQuery("org_sel_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        super.addWhereClauseForId(whereClauses, tenantId);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        fullQuery += " order by label asc";
        Map<String, Object> params = super.buildParams(whereClauses);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, new RowMapper<>() {
            @Override
            public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
                return OrganizationExtractor.extractOrganization(rs, false, tenantId);
            }
        });
    }

    @Override
    public Organization findByCode(String code) {
        String baseQuery = super.getQuery("org_sel_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_CODE, DaoConstants.P_CODE))
                .paramName(DaoConstants.P_CODE)
                .operator(WhereOperator.AND)
                .paramValue(code)
                .build());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return OrganizationExtractor.extractOrganization(rs, true, null);
        });
    }

}
