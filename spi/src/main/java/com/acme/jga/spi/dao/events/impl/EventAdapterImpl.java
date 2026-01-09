package com.acme.jga.spi.dao.events.impl;

import com.acme.jga.domain.model.event.AuditEvent;
import com.acme.jga.domain.model.event.EventStatus;
import com.acme.jga.spi.adapter.event.impl.EventAdapter;
import com.acme.jga.spi.dao.events.api.EventsDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventAdapterImpl implements EventAdapter {
    private final EventsDao eventsDao;

    public EventAdapterImpl(EventsDao eventsDao) {
        this.eventsDao = eventsDao;
    }

    @Override
    public List<AuditEvent> findPendingEvents() {
        return this.eventsDao.findPendingEvents();
    }

    @Override
    public Integer updateEvents(List<String> eventsUidList, EventStatus eventStatus) {
        return eventsDao.updateEvents(eventsUidList, EventStatus.PROCESSED);
    }
}
