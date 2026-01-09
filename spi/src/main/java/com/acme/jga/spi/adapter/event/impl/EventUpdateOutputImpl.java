package com.acme.jga.spi.adapter.event.impl;

import com.acme.jga.domain.exceptions.TechnicalException;
import com.acme.jga.domain.model.event.AuditAuthor;
import com.acme.jga.domain.model.event.AuditEvent;
import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.event.EventStatus;
import com.acme.jga.domain.output.functions.events.EventUpdateOutput;
import com.acme.jga.spi.dao.events.api.EventsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@Transactional
public class EventUpdateOutputImpl implements EventUpdateOutput {
    private final EventsDao eventsDao;
    private final JsonMapper jsonMapper;

    public EventUpdateOutputImpl(EventsDao eventsDao, JsonMapper jsonMapper) {
        this.eventsDao = eventsDao;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void saveChanges(EventData eventData) {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setChanges(eventData.auditChanges());
        auditEvent.setCreatedAt(LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime());
        auditEvent.setLastUpdatedAt(LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime());
        auditEvent.setAction(eventData.auditAction());
        auditEvent.setAuthor(AuditAuthor.builder().name(eventData.contextUser()).build());
        auditEvent.setStatus(EventStatus.PENDING);
        auditEvent.setScope(eventData.scope());
        auditEvent.setTarget(eventData.target());
        auditEvent.setObjectUid(eventData.objectUid());
        auditEvent.setUid(UUID.randomUUID().toString());
        String serialiazedEvent = jsonMapper.writeValueAsString(auditEvent);
        auditEvent.setPayload(serialiazedEvent);
        try {
            eventsDao.save(auditEvent);
        } catch (SQLException e) {
            throw new TechnicalException("", e);
        }
    }
}
