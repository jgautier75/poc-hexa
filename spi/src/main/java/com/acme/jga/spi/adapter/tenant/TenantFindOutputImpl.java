package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantFindOutput;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantFindOutputImpl implements TenantFindOutput {
    private final TenantsDao tenantsDao;

    public TenantFindOutputImpl(TenantsDao tenantsDao) {
        this.tenantsDao = tenantsDao;
    }

    @Override
    public Tenant findByCode(String code) {
        Tenant rdbmsTenant = this.tenantsDao.findByCode(code);
        return Optional.ofNullable(rdbmsTenant).map(rt -> new Tenant(rdbmsTenant.id(), rdbmsTenant.code(), rdbmsTenant.label(), rdbmsTenant.status())).orElse(null);
    }

    @Override
    public Tenant findByExternalId(CompositeId tenantId) {
        Tenant rdbmsTenant = this.tenantsDao.findByExternalId(tenantId.get());
        return Optional.ofNullable(rdbmsTenant).map(rt -> new Tenant(rdbmsTenant.id(), rdbmsTenant.code(), rdbmsTenant.label(), rdbmsTenant.status())).orElse(null);
    }

    @Override
    public Tenant findById(CompositeId tenantId) throws FunctionalException {
        return this.tenantsDao.findById(tenantId);
    }
}
