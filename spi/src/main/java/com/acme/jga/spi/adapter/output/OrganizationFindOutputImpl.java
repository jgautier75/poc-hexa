package com.acme.jga.spi.adapter.output;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationFindOutputImpl implements OrganizationFindOutput {
    private final OrganizationsDao organizationsDao;

    public OrganizationFindOutputImpl(OrganizationsDao organizationsDao) {
        this.organizationsDao = organizationsDao;
    }

    @Override
    public Organization findById(CompositeId tenantId, CompositeId organizationId) {
        return this.organizationsDao.findByTenantAndId(tenantId, organizationId);
    }

    @Override
    public boolean existsByCode(String code) {
        return this.organizationsDao.existsByCode(code);
    }

    @Override
    public List<Organization> findAll(CompositeId tenantId) {
        return this.organizationsDao.findAll(tenantId);
    }

    @Override
    public Organization findByCode(String code) {
        return this.organizationsDao.findByCode(code);
    }
}
