package com.acme.jga.spi.dao.tenant.impl;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.spi.dao.tenant.api.TenantsDao;
import com.acme.jga.spi.jdbc.extractors.TenantsDbExtractor;
import com.acme.jga.spi.jdbc.model.RdbmsTenant;
import com.acme.jga.spi.jdbc.model.RdbmsTenantCreate;
import com.acme.jga.spi.jdbc.utils.AbstractJdbcDaoSupport;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import com.acme.jga.spi.jdbc.utils.WhereClause;
import com.acme.jga.spi.jdbc.utils.WhereOperator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TenantsDaoImpl extends AbstractJdbcDaoSupport implements TenantsDao {

    public TenantsDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        super.loadQueryFilePath(TenantsDaoImpl.class.getClassLoader(), new String[]{"tenants.properties"});
    }

    @Override
    public CompositeId save(RdbmsTenantCreate tenantCreate) {
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
        return new CompositeId(IdKind.BOTH, generatedId, uuid);
    }

    @Override
    public boolean existsByCode(String code) {
        String baseQuery = super.getQuery("tenant_exists_by_code");
        List<WhereClause> whereClauses = whereClauseForCode(code);
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return super.executeExists(baseQuery, params);
        });
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        String baseQuery = super.getQuery("tenant_exists_by_uid");
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLInExpression(DaoConstants.FIELD_UID, DaoConstants.P_UID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_UID)
                .paramValue(externalId).build());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return super.executeExists(baseQuery, params);
        });
    }


    @Override
    public RdbmsTenant findByCode(String code) {
        String baseQuery = super.getQuery("tenant_sel_base");
        List<WhereClause> whereClauses = whereClauseForCode(code);
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return TenantsDbExtractor.extractTenant(rs, true);
        });
    }

    @Override
    public List<RdbmsTenant> findAll() {
        String baseQuery = super.getQuery("tenant_sel_base");
        baseQuery += " order by label asc";
        return super.getNamedParameterJdbcTemplate().query(baseQuery, new RowMapper<>() {
            @Override
            @Nullable
            public RdbmsTenant mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                return TenantsDbExtractor.extractTenant(rs, false);
            }
        });
    }

    @Override
    public boolean update(RdbmsTenant tenant) {
        String baseQuery = super.getQuery("tenant_update");
        List<WhereClause> whereClauses = new ArrayList<>();
        if (tenant.compositeId().idKind() == IdKind.BOTH || tenant.compositeId().idKind() == IdKind.NUMBER_ONLY) {
            whereClauses.add(WhereClause.builder()
                    .expression(buildSQLInExpression(DaoConstants.FIELD_ID, DaoConstants.P_ID))
                    .operator(WhereOperator.AND)
                    .paramName(DaoConstants.P_ID)
                    .paramValue(tenant.compositeId().externalId()).build());
        } else {
            whereClauses.add(WhereClause.builder()
                    .expression(buildSQLInExpression(DaoConstants.FIELD_UID, DaoConstants.P_UID))
                    .operator(WhereOperator.AND)
                    .paramName(DaoConstants.P_UID)
                    .paramValue(tenant.compositeId().externalId()).build());
        }
        Map<String, Object> params = super.buildParams(whereClauses);
        params.put(DaoConstants.P_CODE, tenant.code());
        params.put(DaoConstants.P_LABEL, tenant.label());
        params.put(DaoConstants.P_STATUS, tenant.tenantStatus().getValue());
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().update(fullQuery, params) > 0;
    }

    @Override
    public boolean delete(CompositeId id) {
        String baseQuery = super.getQuery("tenant_delete_root");
        List<WhereClause> whereClauses = new ArrayList<>();
        if (id.idKind() == IdKind.BOTH || id.idKind() == IdKind.NUMBER_ONLY) {
            whereClauses.add(WhereClause.builder()
                    .expression(buildSQLInExpression(DaoConstants.FIELD_ID, DaoConstants.P_ID))
                    .operator(WhereOperator.AND)
                    .paramName(DaoConstants.P_ID)
                    .paramValue(id.externalId()).build());
        } else {
            whereClauses.add(WhereClause.builder()
                    .expression(buildSQLInExpression(DaoConstants.FIELD_UID, DaoConstants.P_UID))
                    .operator(WhereOperator.AND)
                    .paramName(DaoConstants.P_UID)
                    .paramValue(id.externalId()).build());
        }
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        Map<String, Object> params = super.buildParams(whereClauses);
        return super.getNamedParameterJdbcTemplate().update(fullQuery, params) > 1;
    }

    private List<WhereClause> whereClauseForCode(String code) {
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLInExpression(DaoConstants.FIELD_CODE, DaoConstants.P_CODE))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_CODE)
                .paramValue(code).build());
        return whereClauses;
    }

}
