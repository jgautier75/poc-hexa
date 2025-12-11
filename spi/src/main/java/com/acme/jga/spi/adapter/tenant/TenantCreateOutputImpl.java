package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantCreateOutput;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TenantCreateOutputImpl implements TenantCreateOutput {
    private final TenantsDao tenantsDao;

    public TenantCreateOutputImpl(TenantsDao tenantsDao) {
        this.tenantsDao = tenantsDao;
    }

    @Override
    public CompositeId save(Tenant tenant) {
        return tenantsDao.save(convert(tenant));
    }

    private Tenant convert(Tenant tenant) {
        return new Tenant(null, tenant.code(), tenant.label(), tenant.status());
    }
}
