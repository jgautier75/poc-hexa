package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.organizations.OrganizationCreateOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import com.acme.jga.spi.dao.sectors.api.SectorsDao;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationCreateOutputImpl implements OrganizationCreateOutput {
    private final OrganizationsDao organizationsDao;
    private final SectorsDao sectorsDao;
    private final TenantsDao tenantsDao;

    public OrganizationCreateOutputImpl(OrganizationsDao organizationsDao, SectorsDao sectorsDao, TenantsDao tenantsDao) {
        this.organizationsDao = organizationsDao;
        this.sectorsDao = sectorsDao;
        this.tenantsDao = tenantsDao;
    }

    @Override
    public CompositeId save(Organization organization) {
        CompositeId compositeId = this.organizationsDao.save(organization);
        Tenant tenant = this.tenantsDao.findByExternalId(organization.tenantId().externalId());

        // Always create a root sector for organization
        Sector sector = new Sector(null, tenant.id(), compositeId, organization.label(), organization.code(), null, true, null);
        this.sectorsDao.save(sector);
        return compositeId;
    }

}
