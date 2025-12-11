package com.acme.jga.spi.adapter.output;

import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.organizations.OrganizationUpdateOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationUpdateOutputImpl implements OrganizationUpdateOutput {
    private final OrganizationsDao organizationsDao;

    public OrganizationUpdateOutputImpl(OrganizationsDao organizationsDao) {
        this.organizationsDao = organizationsDao;
    }

    @Override
    public Integer update(Organization organization) {
        return this.organizationsDao.update(organization.tenantId(), organization.id(),
                organization.code(), organization.label(), organization.country(), organization.status());
    }
}
