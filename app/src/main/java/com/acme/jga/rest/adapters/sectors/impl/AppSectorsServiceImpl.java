package com.acme.jga.rest.adapters.sectors.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.sectors.SectorCreateInput;
import com.acme.jga.domain.input.functions.sectors.SectorsListInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.shared.StreamUtil;
import com.acme.jga.rest.dtos.v1.sectors.SectorDisplayDto;
import com.acme.jga.rest.dtos.v1.sectors.SectorDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.adapters.sectors.api.AppSectorsService;
import org.springframework.stereotype.Service;

@Service
public class AppSectorsServiceImpl implements AppSectorsService {

    private final SectorCreateInput sectorCreateInput;
    private final SectorsListInput sectorsListInput;

    public AppSectorsServiceImpl(SectorCreateInput sectorCreateInput, SectorsListInput sectorsListInput) {
        this.sectorCreateInput = sectorCreateInput;
        this.sectorsListInput = sectorsListInput;
    }

    @Override
    public SectorDisplayDto findSectorHierarchy(String tenantId, String organizationUid) throws FunctionalException {
        Sector sectorHierarchy = this.sectorsListInput.findSectorHierarchy(new CompositeId(null, tenantId), new CompositeId(null, organizationUid));
        SectorDisplayDto root = convert(sectorHierarchy);
        convertRecursively(root, sectorHierarchy);
        return root;
    }

    @Override
    public UidDto createSector(String tenantUid, String organizationUid, SectorDto sectorDto) throws FunctionalException {
        Sector sector = new Sector(null, new CompositeId(null, tenantUid), new CompositeId(null, organizationUid), sectorDto.getLabel(), sectorDto.getCode(), new CompositeId(null, sectorDto.getParentUid()), false, null);
        CompositeId compositeId = sectorCreateInput.create(sector);
        return new UidDto(compositeId.externalId());
    }

    private void convertRecursively(SectorDisplayDto parent, Sector domainSector) {
        StreamUtil.ofNullableList(domainSector.getChildren()).forEach(childSector -> {
            SectorDisplayDto childDto = convert(childSector);
            parent.getChildren().add(childDto);
            convertRecursively(childDto, childSector);
        });
    }

    private SectorDisplayDto convert(Sector sector) {
        return new SectorDisplayDto().toBuilder()
                .uid(sector.getId().externalId())
                .code(sector.getCode())
                .label(sector.getLabel())
                .build();
    }

}
