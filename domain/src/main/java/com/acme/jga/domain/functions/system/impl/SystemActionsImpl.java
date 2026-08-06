package com.acme.jga.domain.functions.system.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.ports.primary.system.SystemActionsInput;
import com.acme.jga.domain.ports.secondary.system.SystemOrganizationsMigrate;
import com.acme.jga.domain.ports.secondary.system.SystemUsersMigrate;

@DomainService
public class SystemActionsImpl implements SystemActionsInput {
    private final EventPublisher eventPublisher;
    private final SystemUsersMigrate systemUsersMigrate;
    private final SystemOrganizationsMigrate systemOrganizationsMigrate;

    public SystemActionsImpl(EventPublisher eventPublisher,
                             SystemUsersMigrate systemUsersMigrate,
                             SystemOrganizationsMigrate systemOrganizationsMigrate) {
        this.eventPublisher = eventPublisher;
        this.systemUsersMigrate = systemUsersMigrate;
        this.systemOrganizationsMigrate = systemOrganizationsMigrate;
    }

    @Override
    public void pushAuditEvents() {
        eventPublisher.pushAuditEvents();
    }

    @Override
    public void migrateUsersDiacritic() {
        systemUsersMigrate.migrateDiacritic();
    }

    @Override
    public void migrateOrganizationsDiacritic() {
        systemOrganizationsMigrate.migrateDiacritic();
    }
}
