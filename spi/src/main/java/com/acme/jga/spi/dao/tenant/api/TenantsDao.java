package com.acme.jga.spi.dao.tenant.api;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.spi.jdbc.model.RdbmsTenant;
import com.acme.jga.spi.jdbc.model.RdbmsTenantCreate;

import java.util.List;

public interface TenantsDao {

    CompositeId save(RdbmsTenantCreate tenantCreate);

    boolean existsByCode(String code);

    boolean existsByExternalId(String externalId);

    RdbmsTenant findByCode(String code);

    RdbmsTenant findByExternalId(String externalId);

    List<RdbmsTenant> findAll();

    boolean update(RdbmsTenant tenant);

    boolean delete(CompositeId id);

}
