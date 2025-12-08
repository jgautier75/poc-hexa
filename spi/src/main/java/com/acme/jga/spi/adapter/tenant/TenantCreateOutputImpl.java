package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantCreate;
import com.acme.jga.spi.dao.tenant.api.TenantsDao;
import com.acme.jga.spi.jdbc.model.RdbmsTenantCreate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TenantCreateImpl implements TenantCreate {
    private final TenantsDao tenantsDao;

    public TenantCreateImpl(TenantsDao tenantsDao) {
        this.tenantsDao = tenantsDao;
    }

    @Override
    public CompositeId save(Tenant tenant) {
        return tenantsDao.save(convert(tenant));
    }

    private RdbmsTenantCreate convert(Tenant tenant) {
        return new RdbmsTenantCreate(tenant.code(), tenant.label(), tenant.tenantStatus());
    }
}
