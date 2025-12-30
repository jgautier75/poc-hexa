package com.acme.jga.spi.dao.users.impl;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sorting.OrderByClause;
import com.acme.jga.domain.model.sorting.OrderDirection;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.spi.dao.tenants.impl.TenantsDaoImpl;
import com.acme.jga.spi.dao.users.api.UsersDao;
import com.acme.jga.spi.jdbc.extractors.UserExtractor;
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
import java.util.*;

@Repository
public class UsersDaoImpl extends AbstractJdbcDaoSupport implements UsersDao {

    public UsersDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, ObservationRegistry observationRegistry, SdkLoggerProvider sdkLoggerProvider) {
        super(observationRegistry, namedParameterJdbcTemplate, sdkLoggerProvider);
        super.loadQueryFilePath(TenantsDaoImpl.class.getClassLoader(), new String[]{"users.properties"});
    }

    @Override
    public CompositeId create(User user) {
        String baseQuery = super.getQuery("user_create");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String uuid = DaoConstants.generatedUUID();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        //insert into users (tenant_id,uid,org_id,login,email,first_name,last_name, middle_name,status,secrets, notif_email) values(?,?,?,?,?,?,?,?,?,?)
        mapSqlParameterSource.addValue(DaoConstants.P_TENANT_ID, user.tenantId().internalId());
        mapSqlParameterSource.addValue(DaoConstants.P_UID, uuid);
        mapSqlParameterSource.addValue(DaoConstants.P_ORG_ID, user.organizationId().internalId());
        mapSqlParameterSource.addValue(DaoConstants.P_LOGIN, user.login());
        mapSqlParameterSource.addValue(DaoConstants.P_EMAIL, user.email());
        mapSqlParameterSource.addValue(DaoConstants.P_FIRST_NAME, user.firstName());
        mapSqlParameterSource.addValue(DaoConstants.P_LAST_NAME, user.lastName());
        mapSqlParameterSource.addValue(DaoConstants.P_MIDDLE_NAME, user.middleName());
        mapSqlParameterSource.addValue(DaoConstants.P_STATUS, user.status().getValue());
        mapSqlParameterSource.addValue(DaoConstants.P_SECRETS, user.secrets());
        mapSqlParameterSource.addValue(DaoConstants.P_NOTIF, user.notifEmail());
        super.getNamedParameterJdbcTemplate().update(baseQuery, mapSqlParameterSource, keyHolder);
        Long generatedId = super.extractGeneratedId(keyHolder, DaoConstants.FIELD_ID);
        return new CompositeId(generatedId, uuid);
    }

    @Override
    public boolean emailExists(String email) {
        String baseQuery = super.getQuery("user_count");
        List<WhereClause> whereClauses = List.of(WhereClause.builder()
                .operator(WhereOperator.AND)
                .expression(super.buildSQLEqualsExpression(DaoConstants.FIELD_EMAIL, DaoConstants.P_EMAIL))
                .paramName(DaoConstants.P_EMAIL)
                .paramValue(email).build());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, Collections.emptyList());
        return super.executeExists(fullQuery, params);
    }

    @Override
    public boolean loginExists(String login) {
        String baseQuery = super.getQuery("user_count");
        List<WhereClause> whereClauses = List.of(WhereClause.builder()
                .expression(super.buildSQLEqualsExpression(DaoConstants.FIELD_LOGIN, DaoConstants.P_LOGIN))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_LOGIN)
                .paramValue(login).build());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, Collections.emptyList());
        return super.executeExists(fullQuery, params);
    }

    @Override
    public User findById(CompositeId id, CompositeId tenantId, CompositeId organizationId) {
        String baseQuery = super.getQuery("user_sel_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(
                WhereClause.builder()
                        .expression(super.buildSQLEqualsExpression(DaoConstants.FIELD_TENANT_ID, DaoConstants.P_TENANT_ID))
                        .operator(WhereOperator.AND)
                        .paramName(DaoConstants.P_TENANT_ID)
                        .paramValue(tenantId.internalId())
                        .build());
        whereClauses.add(
                WhereClause.builder()
                        .expression(super.buildSQLEqualsExpression(DaoConstants.FIELD_ORG_ID, DaoConstants.P_ORG_ID))
                        .operator(WhereOperator.AND)
                        .paramName(DaoConstants.P_ORG_ID)
                        .paramValue(organizationId.internalId())
                        .build());
        super.addWhereClauseForId(whereClauses, id);
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, Collections.emptyList());
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, rs -> {
            return UserExtractor.extractUser(rs, true, tenantId, organizationId);
        });
    }

    @Override
    public List<User> findAll(CompositeId tenantId, CompositeId organizationId) {
        String baseQuery = super.getQuery("user_sel_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(
                WhereClause.builder()
                        .expression(super.buildSQLEqualsExpression(DaoConstants.FIELD_TENANT_ID, DaoConstants.P_TENANT_ID))
                        .operator(WhereOperator.AND)
                        .paramName(DaoConstants.P_TENANT_ID)
                        .paramValue(tenantId.internalId())
                        .build());
        whereClauses.add(
                WhereClause.builder()
                        .expression(super.buildSQLEqualsExpression(DaoConstants.FIELD_ORG_ID, DaoConstants.P_ORG_ID))
                        .operator(WhereOperator.AND)
                        .paramName(DaoConstants.P_ORG_ID)
                        .paramValue(organizationId.internalId())
                        .build());
        Map<String, Object> params = super.buildParams(whereClauses);
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses,
                List.of(
                        OrderByClause.builder().orderDirection(OrderDirection.ASC).expression("last_name").build(),
                        OrderByClause.builder().orderDirection(OrderDirection.ASC).expression("first_name").build()
                ));
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, new RowMapper<>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return UserExtractor.extractUser(rs, false, tenantId, organizationId);
            }
        });
    }

    @Override
    public Integer update(User user) {
        String baseQuery = super.getQuery("user_update");
        List<WhereClause> whereClauses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put(DaoConstants.P_ID, user.id().internalId());
        params.put(DaoConstants.P_TENANT_ID, user.tenantId().internalId());
        params.put(DaoConstants.P_ORG_ID, user.organizationId().internalId());
        params.put(DaoConstants.P_EMAIL, user.email());
        params.put(DaoConstants.P_FIRST_NAME, user.firstName());
        params.put(DaoConstants.P_LAST_NAME, user.lastName());
        params.put(DaoConstants.P_MIDDLE_NAME, user.middleName());
        params.put(DaoConstants.P_STATUS, user.status().getValue());
        params.put(DaoConstants.P_NOTIF, user.notifEmail());
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, Collections.emptyList());
        return super.getNamedParameterJdbcTemplate().update(fullQuery, params);
    }

    @Override
    public Integer deleteUser(CompositeId id, CompositeId tenantId, CompositeId organizationId) {
        String baseQuery = super.getQuery("user_delete");
        Map<String, Object> params = new HashMap<>();
        params.put(DaoConstants.P_ID, id.internalId());
        params.put(DaoConstants.P_TENANT_ID, tenantId.internalId());
        params.put(DaoConstants.P_ORG_ID, organizationId.internalId());
        return super.getNamedParameterJdbcTemplate().update(baseQuery, params);
    }

}
