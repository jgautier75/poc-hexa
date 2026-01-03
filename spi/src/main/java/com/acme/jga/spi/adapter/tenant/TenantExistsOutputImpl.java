package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.tenants.TenantExistsOutput;
import com.acme.jga.spi.dao.tenants.api.TenantsDao;
import org.springframework.stereotype.Service;

@Service
public class TenantExistsOutputImpl implements TenantExistsOutput {
    private final TenantsDao tenantsDao;

    public TenantExistsOutputImpl(TenantsDao tenantsDao) {
        this.tenantsDao = tenantsDao;
    }

    @Override
    public boolean existsByCode(String code) {
        return this.tenantsDao.existsByCode(code);
    }

    @Override
    public boolean existsByExternalId(String externalId) throws FunctionalException {
        return this.tenantsDao.existsByExternalId(externalId);
    }

    @Override
    public boolean existsById(CompositeId compositeId) throws FunctionalException {
        return this.tenantsDao.existsById(compositeId);
    }
}
