package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.organizations.OrganizationDeleteOutput;
import com.acme.jga.domain.output.functions.tenants.TenantDeleteOuput;
import com.acme.jga.domain.output.functions.users.UserDeleteOutput;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TenantDeleteOutputImpl implements TenantDeleteOuput {
    private final TenantsDao tenantsDao;
    private final OrganizationDeleteOutput organizationDeleteOutput;

    public TenantDeleteOutputImpl(TenantsDao tenantsDao,
                                  OrganizationDeleteOutput organizationDeleteOutput) {
        this.tenantsDao = tenantsDao;
        this.organizationDeleteOutput = organizationDeleteOutput;
    }

    @Override
    public boolean deleteTenant(CompositeId compositeId) {
        organizationDeleteOutput.deleteByTenant(compositeId);
        return tenantsDao.delete(compositeId);
    }
}
