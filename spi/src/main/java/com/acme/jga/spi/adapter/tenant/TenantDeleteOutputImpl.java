package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.tenants.TenantDeleteOuput;
import com.acme.jga.spi.dao.tenant.api.TenantsDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TenantDeleteOutputImpl implements TenantDeleteOuput {
    private final TenantsDao tenantsDao;

    public TenantDeleteOutputImpl(TenantsDao tenantsDao) {
        this.tenantsDao = tenantsDao;
    }

    @Override
    public boolean deleteTenant(CompositeId compositeId) {
        return tenantsDao.delete(compositeId);
    }
}
