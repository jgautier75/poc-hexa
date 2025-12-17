package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.organizations.OrganizationDeleteOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationDeleteOutputImpl implements OrganizationDeleteOutput {
    private final OrganizationsDao organizationsDao;

    public OrganizationDeleteOutputImpl(OrganizationsDao organizationsDao) {
        this.organizationsDao = organizationsDao;
    }

    @Override
    public Integer delete(CompositeId tenantId, CompositeId orgId) {
        return this.organizationsDao.delete(tenantId, orgId);
    }
}
