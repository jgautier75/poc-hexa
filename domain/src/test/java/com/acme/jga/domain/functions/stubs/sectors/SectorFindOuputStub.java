package com.acme.jga.domain.functions.stubs.sectors;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.sectors.SectorFindOuput;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SectorFindOuputStub implements SectorFindOuput {

    private final Tenant tenant;
    private final Organization organization;
    private List<Sector> sectors = new ArrayList<>();

    public SectorFindOuputStub(Tenant tenant, Organization organization) {
        this.tenant = tenant;
        this.organization = organization;
        initSectors(tenant, organization);
    }

    @Override
    public List<Sector> findAll(CompositeId tenantId, CompositeId organizationId) {
        return this.sectors;
    }

    @Override
    public Sector findById(CompositeId tenantId, CompositeId organizationId, CompositeId sectorId) {
        return this.sectors.stream().filter(sector -> sector.getId().equals(sectorId)).findFirst().orElse(null);
    }

    private void initSectors(Tenant tenant, Organization organization) {
        Sector root = new Sector(new CompositeId(3L, UUID.randomUUID().toString()), tenant.id(), organization.id(), "points_cardinaux", "pcards", null, true, null);
        Sector north = new Sector(new CompositeId(4L, UUID.randomUUID().toString()), tenant.id(), organization.id(), "north", "north", root.getId(), false, null);
        Sector south = new Sector(new CompositeId(5L, UUID.randomUUID().toString()), tenant.id(), organization.id(), "south", "south", root.getId(), false, null);
        Sector east = new Sector(new CompositeId(6L, UUID.randomUUID().toString()), tenant.id(), organization.id(), "east", "east", root.getId(), false, null);
        Sector west = new Sector(new CompositeId(7L, UUID.randomUUID().toString()), tenant.id(), organization.id(), "west", "west", root.getId(), false, null);
        Sector northEast = new Sector(new CompositeId(8L, UUID.randomUUID().toString()), tenant.id(), organization.id(), "northeast", "northeast", north.getId(), false, null);
        Sector northNorthEast = new Sector(new CompositeId(9L, UUID.randomUUID().toString()), tenant.id(), organization.id(), "northnortheast", "northnortheast", northEast.getId(), false, null);
        Sector northWest = new Sector(new CompositeId(10L, UUID.randomUUID().toString()), tenant.id(), organization.id(), "northwest", "northwest", north.getId(), false, null);
        this.sectors.addAll(List.of(root, north, south, east, west, northEast, northNorthEast, northWest));
    }
}
