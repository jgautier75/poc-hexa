package com.acme.jga.domain.functions.system.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.input.functions.system.SystemActionsInput;

@DomainService
public class SystemActionsImpl implements SystemActionsInput {
    private final EventPublisher eventPublisher;

    public SystemActionsImpl(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void pushAuditEvents() {
        eventPublisher.pushAuditEvents();
    }
}
