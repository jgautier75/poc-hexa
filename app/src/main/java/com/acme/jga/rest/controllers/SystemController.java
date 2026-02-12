package com.acme.jga.rest.controllers;

import com.acme.jga.rest.adapters.system.api.AppSystemService;
import com.acme.jga.rest.dtos.v1.versions.ApiVersionList;
import com.acme.jga.rest.utils.WebApiVersions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {
    private final AppSystemService appSystemService;

    public SystemController(AppSystemService appSystemService) {
        this.appSystemService = appSystemService;
    }

    @GetMapping(value = WebApiVersions.SystemResourceVersion.VERSIONS)
    public ResponseEntity<ApiVersionList> listVersions() {
        ApiVersionList versions = appSystemService.versions();
        return new ResponseEntity<>(versions, HttpStatus.OK);
    }

    @PostMapping(value = WebApiVersions.SystemResourceVersion.KAFKA_WAKEUP)
    public ResponseEntity<Void> kafkaWakeUp() {
        appSystemService.kafkaWakeup();
        return ResponseEntity.ok().build();
    }

}
