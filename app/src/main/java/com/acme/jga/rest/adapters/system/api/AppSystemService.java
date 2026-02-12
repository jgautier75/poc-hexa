package com.acme.jga.rest.adapters.system.api;

import com.acme.jga.rest.dtos.v1.versions.ApiVersionList;

public interface AppSystemService {
    ApiVersionList versions();

    void kafkaWakeup();
}
