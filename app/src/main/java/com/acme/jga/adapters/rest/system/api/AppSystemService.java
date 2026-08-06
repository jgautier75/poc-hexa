package com.acme.jga.adapters.rest.system.api;

import com.acme.jga.rest.dtos.v1.versions.ApiVersionList;

public interface AppSystemService {
    ApiVersionList versions();

    void kafkaWakeup();

    void migrateUsersDiacritic();

    void migrateOrganizationsDiacritic();

}
