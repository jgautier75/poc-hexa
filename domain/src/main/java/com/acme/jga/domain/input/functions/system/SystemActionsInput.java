package com.acme.jga.domain.input.functions.system;

public interface SystemActionsInput {

    void pushAuditEvents();

    void migrateUsersDiacritic();

    void migrateOrganizationsDiacritic();

}
