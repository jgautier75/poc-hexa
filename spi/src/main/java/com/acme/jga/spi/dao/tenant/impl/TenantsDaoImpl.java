package com.acme.jga.spi.dao.tenant.impl;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.spi.dao.tenant.api.TenantsDao;
import com.acme.jga.spi.jdbc.AbstractJdbcDaoSupport;
import com.acme.jga.spi.model.RdbmsTenantCreate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TenantDaoImpl extends AbstractJdbcDaoSupport implements TenantsDao {

    public TenantDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public CompositeId save(RdbmsTenantCreate tenantCreate) {
        return null;
    }
}
