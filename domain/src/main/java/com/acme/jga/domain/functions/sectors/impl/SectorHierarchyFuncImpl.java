package com.acme.jga.domain.functions.sectors.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.output.functions.sectors.SectorFindOuput;
import com.acme.jga.domain.input.functions.sectors.SectorsListInput;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.shared.StreamUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@DomainService
public class SectorHierarchyFuncImpl implements SectorsListInput {

    private final TenantFindInput tenantFindInput;
    private final OrganizationFindInput organizationFindInput;
    private final SectorFindOuput sectorFindOuput;

    public SectorHierarchyFuncImpl(TenantFindInput tenantFindInput, OrganizationFindInput organizationFindInput, SectorFindOuput sectorFindOuput) {
        this.tenantFindInput = tenantFindInput;
        this.organizationFindInput = organizationFindInput;
        this.sectorFindOuput = sectorFindOuput;
    }

    @Override
    public Sector findSectorHierarchy(CompositeId tenantId, CompositeId organizationId) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(tenantId, null);
        Organization organization = organizationFindInput.findById(tenantId, organizationId);
        List<Sector> allSectors = sectorFindOuput.findAll(tenant.id(), organization.id());
        Optional<Sector> rootSector = StreamUtil.ofNullableList(allSectors).filter(Sector::isRoot).findFirst();
        rootSector.ifPresent(rt -> mapSectorsRecursively(rt, allSectors));
        return rootSector.orElse(null);
    }

    private void mapSectorsRecursively(Sector parentSector, List<Sector> sectors) {
        List<Sector> children = sectors.stream()
                .filter(sect -> sect.getParent() != null && sect.getParent().internalId().equals(parentSector.getId().internalId()))
                .sorted(Comparator.comparing(Sector::getLabel))
                .toList();
        children.forEach(child -> {
            parentSector.addChild(child);
            mapSectorsRecursively(child, sectors);
        });
    }

}
