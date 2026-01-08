package com.acme.jga.spi.dao.events.api;

import com.acme.jga.domain.model.event.AuditEvent;

import java.sql.SQLException;

public interface EventsDao {
    void save(AuditEvent auditEvent) throws SQLException;
}
