package com.acme.jga.spi.dao.tenants.impl;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import com.acme.jga.spi.jdbc.extractors.TenantExtractor;
import com.acme.jga.spi.jdbc.utils.AbstractJdbcDaoSupport;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import com.acme.jga.spi.jdbc.utils.WhereClause;
import com.acme.jga.spi.jdbc.utils.WhereOperator;
import io.micrometer.observation.annotation.Observed;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TenantsDaoImpl extends AbstractJdbcDaoSupport implements TenantsDao {

    public TenantsDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        super.loadQueryFilePath(TenantsDaoImpl.class.getClassLoader(), new String[]{"tenants.properties"});
    }

    @Override
    public CompositeId save(Tenant tenantCreate) {
        String baseQuery = super.getQuery("tenant_create");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String uuid = DaoConstants.generatedUUID();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(DaoConstants.P_UID, uuid);
        mapSqlParameterSource.addValue(DaoConstants.P_CODE, tenantCreate.code());
        mapSqlParameterSource.addValue(DaoConstants.P_LABEL, tenantCreate.label());
        mapSqlParameterSource.addValue(DaoConstants.P_STATUS, tenantCreate.status().getValue());
        super.getNamedParameterJdbcTemplate().update(baseQuery, mapSqlParameterSource, keyHolder);
        Long generatedId = super.extractGeneratedId(keyHolder, DaoConstants.FIELD_ID);
        return new CompositeId(generatedId, uuid);
    }

    @Override
    public boolean existsById(CompositeId id) {
        String baseQuery = super.getQuery("tenant_exists_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        super.addWhereClauseForId(whereClauses, id);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null);
        Map<String, Object> params = super.buildParams(whereClauses);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return super.executeExists(baseQuery, params);
        });
    }

    @Override
    public boolean existsByCode(String code) {
        String baseQuery = super.getQuery("tenant_exists_by_code");
        Map<String, Object> params = new HashMap<>();
        params.put(DaoConstants.P_CODE, code);
        return super.getNamedParameterJdbcTemplate().query(baseQuery, params, rs -> {
            return super.executeExists(baseQuery, params);
        });
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        String baseQuery = super.getQuery("tenant_exists_by_uid");
        Map<String, Object> params = new HashMap<>();
        params.put(DaoConstants.P_UID, externalId);
        return super.getNamedParameterJdbcTemplate().query(baseQuery, params, rs -> {
            return super.executeExists(baseQuery, params);
        });
    }


    @Override
    public Tenant findByCode(String code) {
        String baseQuery = super.getQuery("tenant_sel_base");
        List<WhereClause> whereClauses = whereClauseForCode(code);
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return TenantExtractor.extractTenant(rs, true);
        });
    }

    @Override
    @Observed(name = "DAO_TENANT_FIND_BY_ID")
    public Tenant findByExternalId(String externalId) {
        String baseQuery = super.getQuery("tenant_sel_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLInExpression(DaoConstants.FIELD_UID, DaoConstants.P_UID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_UID)
                .paramValue(externalId).build());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return TenantExtractor.extractTenant(rs, true);
        });
    }

    @Override
    public Tenant findById(CompositeId id) {
        String baseQuery = super.getQuery("tenant_sel_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        super.addWhereClauseForId(whereClauses, id);
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return TenantExtractor.extractTenant(rs, true);
        });
    }

    @Override
    public List<Tenant> findAll() {
        String baseQuery = super.getQuery("tenant_sel_base");
        baseQuery += " order by label asc";
        return super.getNamedParameterJdbcTemplate().query(baseQuery, new RowMapper<>() {
            @Override
            public Tenant mapRow(ResultSet rs, int rowNum) throws SQLException {
                return TenantExtractor.extractTenant(rs, false);
            }
        });
    }

    @Override
    public boolean update(Tenant tenant) {
        String baseQuery = super.getQuery("tenant_update");
        List<WhereClause> whereClauses = new ArrayList<>();
        addWhereClauseForId(whereClauses, tenant.id());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        params.put(DaoConstants.P_CODE, tenant.code());
        params.put(DaoConstants.P_LABEL, tenant.label());
        params.put(DaoConstants.P_STATUS, tenant.status().getValue());
        return super.getNamedParameterJdbcTemplate().update(fullQuery, params) > 0;
    }

    @Override
    public boolean delete(CompositeId id) {
        String baseQuery = super.getQuery("tenant_delete_root");
        List<WhereClause> whereClauses = new ArrayList<>();
        addWhereClauseForId(whereClauses, id);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        Map<String, Object> params = super.buildParams(whereClauses);
        return super.getNamedParameterJdbcTemplate().update(fullQuery, params) > 1;
    }

}
