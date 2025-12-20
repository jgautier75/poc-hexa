package com.acme.jga.rest.services.sectors.api;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.sectors.SectorDisplayDto;
import com.acme.jga.rest.dtos.v1.sectors.SectorDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;

public interface AppSectorsService {
    SectorDisplayDto findSectorHierarchy(String tenantId, String organizationUid) throws FunctionalException;

    UidDto createSector(String tenantUid, String organizationUid, SectorDto sectorDto) throws FunctionalException;

}
