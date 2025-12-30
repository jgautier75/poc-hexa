package com.acme.jga.rest.controllers;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.organizations.OrganizationDto;
import com.acme.jga.rest.dtos.v1.organizations.OrganizationListDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.services.organizations.api.AppOrganizationsService;
import com.acme.jga.rest.utils.WebApiVersions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrganizationsController {
    private final AppOrganizationsService appOrganizationsService;

    public OrganizationsController(AppOrganizationsService appOrganizationsService) {
        this.appOrganizationsService = appOrganizationsService;
    }

    @PostMapping(value = WebApiVersions.OrganizationsResourceVersion.ROOT)
    public ResponseEntity<com.acme.jga.rest.dtos.v1.tenants.UidDto> createOrganization(@PathVariable("tenantUid") String tenantUid,
                                                                                       @RequestBody OrganizationDto organizationDto) throws FunctionalException {
        UidDto uidDto = appOrganizationsService.createOrganization(tenantUid, organizationDto);
        return new ResponseEntity<>(uidDto, HttpStatus.CREATED);
    }

    @GetMapping(value = WebApiVersions.OrganizationsResourceVersion.ROOT)
    public ResponseEntity<OrganizationListDisplayDto> findOrgsByTenant(@PathVariable("tenantUid") String tenantUid,
                                                                       @RequestParam(value = "filter", required = false) String searchFilter,
                                                                       @RequestParam(value = "index", required = false, defaultValue = "1") Integer pageIndex,
                                                                       @RequestParam(value = "size", required = false, defaultValue = "10") Integer pageSize,
                                                                       @RequestParam(value = "orderBy", required = false, defaultValue = "label") String orderBy) throws FunctionalException {
        OrganizationListDisplayDto organizationListDisplayDto = appOrganizationsService.listOrganizations(tenantUid);
        return new ResponseEntity<>(organizationListDisplayDto, HttpStatus.OK);
    }

    @GetMapping(value = WebApiVersions.OrganizationsResourceVersion.WITH_UID)
    public ResponseEntity<OrganizationDto> findOrgDetails(@PathVariable("tenantUid") String tenantUid, @PathVariable("orgUid") String orgUid,
                                                          @RequestParam(name = "fetchSectors", defaultValue = "true") boolean fecthSectors) throws FunctionalException {
        OrganizationDto organization = appOrganizationsService.findOrganization(tenantUid, orgUid);
        return new ResponseEntity<>(organization, HttpStatus.OK);
    }

    @PostMapping(value = WebApiVersions.OrganizationsResourceVersion.WITH_UID)
    public ResponseEntity<Void> updateOrganization(@PathVariable("tenantUid") String tenantUid,
                                                   @PathVariable("orgUid") String orgUid,
                                                   @RequestBody OrganizationDto organizationDto) throws FunctionalException {
        appOrganizationsService.updateOrganization(tenantUid, orgUid, organizationDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = WebApiVersions.OrganizationsResourceVersion.WITH_UID)
    public ResponseEntity<Void> deleteOrganization(@PathVariable("tenantUid") String tenantUid,
                                                   @PathVariable("orgUid") String orgUid) throws FunctionalException {
        appOrganizationsService.deleteOrganization(tenantUid, orgUid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
