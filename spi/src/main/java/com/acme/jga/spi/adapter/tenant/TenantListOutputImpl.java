package com.acme.jga.spi.adapter.tenant;

import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantListOutput;
import com.acme.jga.domain.shared.StreamUtil;
import com.acme.jga.spi.dao.tenant.api.TenantsDao;
import com.acme.jga.spi.jdbc.model.RdbmsTenant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantListOutputImpl implements TenantListOutput {

    private final TenantsDao tenantsDao;

    public TenantListOutputImpl(TenantsDao tenantsDao) {
        this.tenantsDao = tenantsDao;
    }

    @Override
    public List<Tenant> list() {
        List<RdbmsTenant> tenants = tenantsDao.findAll();
        return StreamUtil.ofNullableList(tenants).map(rt -> new Tenant(() -> rt.compositeId().externalId(), rt.code(), rt.label(), rt.tenantStatus())).toList();
    }
}
