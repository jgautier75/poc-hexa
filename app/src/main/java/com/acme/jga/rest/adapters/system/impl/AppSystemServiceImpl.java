package com.acme.jga.rest.adapters.system.impl;

import com.acme.jga.domain.input.functions.system.SystemActionsInput;
import com.acme.jga.rest.adapters.system.api.AppSystemService;
import com.acme.jga.rest.dtos.v1.versions.ApiVersionDto;
import com.acme.jga.rest.dtos.v1.versions.ApiVersionList;
import com.acme.jga.rest.utils.WebApiVersions;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AppSystemServiceImpl implements AppSystemService {

    private final SystemActionsInput systemActionsInput;

    public AppSystemServiceImpl(SystemActionsInput systemActionsInput) {
        this.systemActionsInput = systemActionsInput;
    }

    @Override
    public ApiVersionList versions() {
        ApiVersionList versionsList = new ApiVersionList();
        Arrays.stream(WebApiVersions.TenantsResourceVersion.Endpoints.values()).forEach(endp -> versionsList.addApiVersion(new ApiVersionDto(endp.getVersion(), endp.getCategory(), endp.getCode(), endp.getUri())));
        Arrays.stream(WebApiVersions.SectorsResourceVersion.Endpoints.values()).forEach(endp -> versionsList.addApiVersion(new ApiVersionDto(endp.getVersion(), endp.getCategory(), endp.getCode(), endp.getUri())));
        Arrays.stream(WebApiVersions.UsersResourceVersion.Endpoints.values()).forEach(endp -> versionsList.addApiVersion(new ApiVersionDto(endp.getVersion(), endp.getCategory(), endp.getCode(), endp.getUri())));
        Arrays.stream(WebApiVersions.OrganizationsResourceVersion.Endpoints.values()).forEach(endp -> versionsList.addApiVersion(new ApiVersionDto(endp.getVersion(), endp.getCategory(), endp.getCode(), endp.getUri())));
        Arrays.stream(WebApiVersions.SystemResourceVersion.Endpoints.values()).forEach(endp -> versionsList.addApiVersion(new ApiVersionDto(endp.getVersion(), endp.getCategory(), endp.getCode(), endp.getUri())));
        return versionsList;
    }

    @Override
    public void kafkaWakeup() {
        systemActionsInput.pushAuditEvents();
    }
}
