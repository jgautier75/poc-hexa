package com.acme.jga.domain.ports.primary.system;

public interface SystemActionsInput {

    void pushAuditEvents();

    void migrateUsersDiacritic();

    void migrateOrganizationsDiacritic();

}
