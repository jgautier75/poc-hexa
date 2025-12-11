package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantUpdateOutput;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TenantUpdateOutputImpl implements TenantUpdateOutput {

    private final TenantsDao tenantsDao;

    public TenantUpdateOutputImpl(TenantsDao tenantsDao) {
        this.tenantsDao = tenantsDao;
    }

    @Override
    public boolean update(Tenant tenant) {
        CompositeId cid = new CompositeId(null, tenant.id().externalId());
        Tenant rdbmsTenant = new Tenant(cid, tenant.code(), tenant.label(), tenant.status());
        return tenantsDao.update(rdbmsTenant);
    }
}
