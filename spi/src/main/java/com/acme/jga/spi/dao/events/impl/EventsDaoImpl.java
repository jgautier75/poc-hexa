package com.acme.jga.spi.dao.events.impl;

import com.acme.jga.domain.model.event.AuditEvent;
import com.acme.jga.domain.model.event.EventStatus;
import com.acme.jga.domain.model.sorting.OrderByClause;
import com.acme.jga.domain.model.sorting.OrderDirection;
import com.acme.jga.spi.dao.events.api.EventsDao;
import com.acme.jga.spi.jdbc.extractors.AuditEventExtractor;
import com.acme.jga.spi.jdbc.utils.AbstractJdbcDaoSupport;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import com.acme.jga.spi.jdbc.utils.WhereClause;
import com.acme.jga.spi.jdbc.utils.WhereOperator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EventsDaoImpl extends AbstractJdbcDaoSupport implements EventsDao {

    protected EventsDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        super.loadQueryFilePath(EventsDaoImpl.class.getClassLoader(), new String[]{"events.properties"});
    }

    @Override
    public void save(AuditEvent auditEvent) throws SQLException {
        String baseQuery = super.getQuery("event_create");
        String uuid = auditEvent.getUid();
        Map<String, Object> params = new HashMap<>();
        params.put(DaoConstants.P_UID, uuid);
        params.put("pCreatedAt", auditEvent.getCreatedAt());
        params.put("pUpdatedAt", auditEvent.getLastUpdatedAt());
        params.put("pTarget", auditEvent.getTarget().ordinal());
        params.put("pObjectUid", auditEvent.getObjectUid());
        params.put("pAction", auditEvent.getAction().name());
        params.put("pStatus", auditEvent.getStatus().ordinal());
        params.put("pPayload", super.buildPGobject(auditEvent.getPayload()));
        super.getNamedParameterJdbcTemplate().update(baseQuery, params);
    }

    @Override
    public List<AuditEvent> findPendingEvents() {
        String baseQuery = super.getQuery("event_sel_base");
        List<WhereClause> whereClauses = new ArrayList<>();
        whereClauses.add(WhereClause.builder()
                .expression(buildSQLNotEqualsExpression(DaoConstants.FIELD_STATUS, DaoConstants.P_STATUS))
                .operator(WhereOperator.AND)
                .paramName(DaoConstants.P_STATUS)
                .paramValue(EventStatus.PROCESSED.ordinal())
                .build());
        Map<String, Object> params = super.buildParams(whereClauses);
        OrderByClause createdAtAsc = OrderByClause.builder().expression("created_at").orderDirection(OrderDirection.ASC).build();
        String fullQuery = super.buildFullQuery(baseQuery, whereClauses, List.of(createdAtAsc), (String[]) null);
        return super.getNamedParameterJdbcTemplate().query(fullQuery, params, new RowMapper<>() {
            @Override
            @Nullable
            public AuditEvent mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                return AuditEventExtractor.extractAuditEvent(rs, false);
            }
        });
    }

    @Override
    public Integer updateEvents(List<String> eventsUidList, EventStatus eventStatus) {
        String updateQuery = super.getQuery("events_update_status");
        Map<String, Object> params = new HashMap<>();
        params.put("pUids", eventsUidList);
        params.put(DaoConstants.P_STATUS, eventStatus.ordinal());
        params.put("pUpdated", LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime());
        return super.getNamedParameterJdbcTemplate().update(updateQuery, params);
    }
}
