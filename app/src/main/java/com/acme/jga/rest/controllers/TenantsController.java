package com.acme.jga.rest.controllers;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.tenants.TenantDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.TenantDto;
import com.acme.jga.rest.dtos.v1.tenants.TenantListDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.services.tenants.api.AppTenantsService;
import com.acme.jga.rest.utils.WebApiVersions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TenantsController {

    private final AppTenantsService appTenantsService;

    public TenantsController(AppTenantsService appTenantsService) {
        this.appTenantsService = appTenantsService;
    }

    @PostMapping(value = WebApiVersions.TenantsResourceVersion.ROOT, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UidDto> createTenant(@RequestBody TenantDto tenantDto) throws FunctionalException {
        UidDto uidDto = appTenantsService.createTenant(tenantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(uidDto);
    }

    @GetMapping(value = WebApiVersions.TenantsResourceVersion.WITH_UID)
    public ResponseEntity<TenantDisplayDto> findTenantByUid(@PathVariable(name = "uid", required = true) String uid)
            throws FunctionalException {
        TenantDisplayDto tenantDto = appTenantsService.findByUid(uid);
        return new ResponseEntity<>(tenantDto, HttpStatus.OK);
    }

    @GetMapping(value = WebApiVersions.TenantsResourceVersion.ROOT)
    public ResponseEntity<TenantListDisplayDto> listTenants() throws FunctionalException {
        TenantListDisplayDto tenantListDisplayDto = appTenantsService.findAll();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tenantListDisplayDto);
    }

    @PostMapping(value = WebApiVersions.TenantsResourceVersion.WITH_UID)
    public ResponseEntity<Void> updateTenantByUid(@PathVariable(name = "uid", required = true) String uid,
                                                  @RequestBody TenantDto tenantDto)
            throws FunctionalException {
        appTenantsService.updateTenant(uid, tenantDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = WebApiVersions.TenantsResourceVersion.WITH_UID)
    public ResponseEntity<Void> deleteTenantByUid(@PathVariable(name = "uid", required = true) String uid)
            throws FunctionalException {
        appTenantsService.deleteTenant(uid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
