package com.acme.jga.rest.controllers;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.sectors.SectorDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.adapters.sectors.api.AppSectorsService;
import com.acme.jga.rest.utils.WebApiVersions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SectorsController {
    private final AppSectorsService appSectorsService;

    public SectorsController(AppSectorsService appSectorsService) {
        this.appSectorsService = appSectorsService;
    }

    @GetMapping(value = WebApiVersions.SectorsResourceVersion.ROOT)
    public ResponseEntity<com.acme.jga.rest.dtos.v1.sectors.SectorDisplayDto> findSectors(@PathVariable("tenantUid") String tenantUid,
                                                                                          @PathVariable("orgUid") String orgUid) throws FunctionalException {
        com.acme.jga.rest.dtos.v1.sectors.SectorDisplayDto sectorHierarchy = appSectorsService.findSectorHierarchy(tenantUid, orgUid);
        return new ResponseEntity<>(sectorHierarchy, HttpStatus.OK);
    }

    @PostMapping(value = WebApiVersions.SectorsResourceVersion.ROOT)
    public ResponseEntity<UidDto> createSector(@PathVariable("tenantUid") String tenantUid,
                                               @PathVariable("orgUid") String orgUid,
                                               @RequestBody SectorDto sectorDto) throws FunctionalException {
        UidDto uidDto = appSectorsService.createSector(tenantUid, orgUid, sectorDto);
        return new ResponseEntity<>(uidDto, HttpStatus.CREATED);
    }
}
