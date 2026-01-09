package com.acme.jga.spi.jdbc.extractors;

import com.acme.jga.domain.model.event.AuditAction;
import com.acme.jga.domain.model.event.AuditEvent;
import com.acme.jga.domain.model.event.EventStatus;
import com.acme.jga.domain.model.event.EventTarget;
import com.acme.jga.spi.jdbc.utils.SQLExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuditEventExtractor {
    public static AuditEvent extractAuditEvent(ResultSet resultSet, boolean checkNext) throws SQLException {
        AuditEvent evt = null;
        if (!checkNext || resultSet.next()) {
            evt = AuditEvent.builder()
                    .action(AuditAction.valueOf(SQLExtractor.extractString(resultSet, "action")))
                    .createdAt(SQLExtractor.extractLocalDateTime(resultSet, "created_at"))
                    .lastUpdatedAt(SQLExtractor.extractLocalDateTime(resultSet, "last_updated_at"))
                    .objectUid(SQLExtractor.extractString(resultSet, "object_uid"))
                    .payload(SQLExtractor.extractString(resultSet, "payload"))
                    .status(EventStatus.values()[SQLExtractor.extractInteger(resultSet, "status")])
                    .target(EventTarget.values()[SQLExtractor.extractInteger(resultSet, "target")])
                    .uid(SQLExtractor.extractString(resultSet, "uid")).build();
        }
        return evt;
    }
}
