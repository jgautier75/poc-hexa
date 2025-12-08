package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantFindOutput;
import com.acme.jga.spi.dao.tenant.api.TenantsDao;
import com.acme.jga.spi.jdbc.model.RdbmsTenant;
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
        RdbmsTenant rdbmsTenant = this.tenantsDao.findByCode(code);
        return Optional.ofNullable(rdbmsTenant).map(rt -> new Tenant(() -> rdbmsTenant.compositeId().externalId(), rdbmsTenant.code(), rdbmsTenant.label(), rdbmsTenant.tenantStatus())).orElse(null);
    }
}
