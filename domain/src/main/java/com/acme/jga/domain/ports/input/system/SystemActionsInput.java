package com.acme.jga.domain.ports.input.system;

public interface SystemActionsInput {

    void pushAuditEvents();

    void migrateUsersDiacritic();

    void migrateOrganizationsDiacritic();

}
