package com.acme.jga.spi.dao.events.impl;

import com.acme.jga.domain.model.event.AuditEvent;
import com.acme.jga.spi.dao.events.api.EventsDao;
import com.acme.jga.spi.jdbc.utils.AbstractJdbcDaoSupport;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
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
}
