package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.output.functions.tenants.TenantExists;
import com.acme.jga.spi.dao.tenant.api.TenantsDao;
import org.springframework.stereotype.Service;

@Service
public class TenantExistsImpl implements TenantExists {
    private final TenantsDao tenantsDao;

    public TenantExistsImpl(TenantsDao tenantsDao) {
        this.tenantsDao = tenantsDao;
    }

    @Override
    public boolean exists(String code) {
        return this.tenantsDao.existsByCode(code);
    }
}
