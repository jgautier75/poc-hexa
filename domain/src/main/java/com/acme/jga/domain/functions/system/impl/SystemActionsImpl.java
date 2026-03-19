package com.acme.jga.domain.functions.system.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.input.functions.system.SystemActionsInput;
import com.acme.jga.domain.output.functions.system.SystemUsersMigrate;

@DomainService
public class SystemActionsImpl implements SystemActionsInput {
    private final EventPublisher eventPublisher;
    private final SystemUsersMigrate systemUsersMigrate;

    public SystemActionsImpl(EventPublisher eventPublisher, SystemUsersMigrate systemUsersMigrate) {
        this.eventPublisher = eventPublisher;
        this.systemUsersMigrate = systemUsersMigrate;
    }

    @Override
    public void pushAuditEvents() {
        eventPublisher.pushAuditEvents();
    }

    @Override
    public void migrateDiacritic() {
        systemUsersMigrate.migrateDiacritic();
    }
}
