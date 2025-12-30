package com.acme.jga.spi.dao.sectors.impl;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.sorting.OrderByClause;
import com.acme.jga.domain.model.sorting.OrderDirection;
import com.acme.jga.spi.dao.tenants.impl.TenantsDaoImpl;
import com.acme.jga.spi.jdbc.extractors.SectorExtractor;
import com.acme.jga.spi.jdbc.utils.AbstractJdbcDaoSupport;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import com.acme.jga.spi.jdbc.utils.WhereClause;
import com.acme.jga.spi.jdbc.utils.WhereOperator;
import io.micrometer.observation.ObservationRegistry;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
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
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
public class SectorsDaoImpl extends AbstractJdbcDaoSupport implements com.acme.jga.spi.dao.sectors.api.SectorsDao {

    public SectorsDaoImpl(ObservationRegistry observationRegistry,
                          NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                          SdkLoggerProvider sdkLoggerProvider) {
        super(observationRegistry, namedParameterJdbcTemplate, sdkLoggerProvider);
        super.loadQueryFilePath(TenantsDaoImpl.class.getClassLoader(), new String[]{"sectors.properties"});
    }

    @Override
    public CompositeId save(Sector sector) {
        String baseQuery = super.getQuery("sector_create");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String uuid = DaoConstants.generatedUUID();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(DaoConstants.P_UID, uuid);
        mapSqlParameterSource.addValue(DaoConstants.P_TENANT_ID, sector.getTenantId().internalId());
        mapSqlParameterSource.addValue(DaoConstants.P_ORG_ID, sector.getOrganizationId().internalId());
        mapSqlParameterSource.addValue(DaoConstants.P_CODE, sector.getCode());
        mapSqlParameterSource.addValue(DaoConstants.P_LABEL, sector.getLabel());
        if (ofNullable(sector.getParent()).isPresent()) {
            mapSqlParameterSource.addValue("pParentId", sector.getParent().internalId());
            mapSqlParameterSource.addValue("pRoot", false);

        } else {
            mapSqlParameterSource.addValue("pParentId", null);
            mapSqlParameterSource.addValue("pRoot", true);
        }
        super.getNamedParameterJdbcTemplate().update(baseQuery, mapSqlParameterSource, keyHolder);
        Long generatedId = super.extractGeneratedId(keyHolder, DaoConstants.FIELD_ID);
        return new CompositeId(generatedId, uuid);
    }

    @Override
    public Integer update(Sector sector) {
        String baseQuery = super.getQuery("sector_update");
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(DaoConstants.P_ID, sector.getId().internalId());
        mapSqlParameterSource.addValue(DaoConstants.P_TENANT_ID, sector.getTenantId().internalId());
        mapSqlParameterSource.addValue(DaoConstants.P_ORG_ID, sector.getOrganizationId().internalId());
        if (Optional.ofNullable(sector.getParent()).isPresent()) {
            mapSqlParameterSource.addValue(DaoConstants.P_PARENT_ID, sector.getParent().internalId());
        } else {
            mapSqlParameterSource.addValue(DaoConstants.P_PARENT_ID, null);
        }
        mapSqlParameterSource.addValue(DaoConstants.P_CODE, sector.getCode());
        mapSqlParameterSource.addValue(DaoConstants.P_LABEL, sector.getLabel());
        return super.getNamedParameterJdbcTemplate().update(baseQuery, mapSqlParameterSource);
    }

    @Override
    public Sector findById(CompositeId tenantId, CompositeId organizationId, CompositeId sectorId) {
        String baseQuery = super.getQuery("sector_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        super.addWhereClauseForId(whereClauses, sectorId);
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_TENANT_ID, DaoConstants.P_TENANT_ID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_TENANT_ID)
                .paramValue(tenantId.internalId()).build());
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_ORG_ID, DaoConstants.P_ORG_ID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_ORG_ID)
                .paramValue(organizationId.internalId()).build());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, null, (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return SectorExtractor.extractSector(rs, true, tenantId, organizationId);
        });
    }

    @Override
    public List<Sector> findAll(CompositeId tenantId, CompositeId organizationId) {
        String baseQuery = super.getQuery("sector_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_TENANT_ID, DaoConstants.P_TENANT_ID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_TENANT_ID)
                .paramValue(tenantId.internalId()).build());
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_ORG_ID, DaoConstants.P_ORG_ID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_ORG_ID)
                .paramValue(organizationId.internalId()).build());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, List.of(OrderByClause.builder().orderDirection(OrderDirection.ASC).expression("label").build()), (String[]) null);

        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, new RowMapper<>() {
            @Override
            public Sector mapRow(ResultSet rs, int rowNum) throws SQLException {
                return SectorExtractor.extractSector(rs, false, tenantId, organizationId);
            }
        });
    }

    @Override
    public Integer delete(CompositeId tenantId, CompositeId organizationId, CompositeId sectorId, CompositeId sectorParentId) {
        String baseQuery = super.getQuery("sector_delete");
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_TENANT_ID, DaoConstants.P_TENANT_ID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_TENANT_ID)
                .paramValue(tenantId.internalId()).build());
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_ORG_ID, DaoConstants.P_ORG_ID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_ORG_ID)
                .paramValue(organizationId.internalId()).build());
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_ID, DaoConstants.P_ID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_ID)
                .paramValue(sectorId.internalId()).build());
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLEqualsExpression(DaoConstants.FIELD_PARENT_ID, DaoConstants.P_PARENT_ID))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_PARENT_ID)
                .paramValue(sectorParentId.internalId()).build());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, List.of(OrderByClause.builder().orderDirection(OrderDirection.ASC).expression("label").build()), (String[]) null);
        return super.getNamedParameterJdbcTemplate().update(fullQuery, params);
    }
}
