package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.organizations.OrganizationCreateOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationCreateOutputImpl implements OrganizationCreateOutput {
    private final OrganizationsDao organizationsDao;

    public OrganizationCreateOutputImpl(OrganizationsDao organizationsDao) {
        this.organizationsDao = organizationsDao;
    }

    @Override
    public CompositeId save(Organization organization) {
        return this.organizationsDao.save(organization);
    }

}
