package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantUpdateOutput;
import com.acme.jga.spi.dao.tenant.api.TenantsDao;
import com.acme.jga.spi.jdbc.model.RdbmsTenant;
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
        CompositeId cid = new CompositeId(IdKind.STRING_ONLY, null, tenant.get());
        RdbmsTenant rdbmsTenant = new RdbmsTenant(cid, tenant.code(), tenant.label(), tenant.tenantStatus());
        return tenantsDao.update(rdbmsTenant);
    }
}
