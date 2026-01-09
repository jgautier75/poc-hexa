package com.acme.jga.spi.adapter.event.impl;

import com.acme.jga.domain.model.event.AuditEvent;
import com.acme.jga.domain.model.event.EventStatus;

import java.util.List;

public interface EventAdapter {
    List<AuditEvent> findPendingEvents();
    Integer updateEvents(List<String> eventsUidList, EventStatus eventStatus);
}
