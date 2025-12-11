package com.acme.jga.spi.dao.tenants.api;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;

import java.util.List;

public interface TenantsDao {

    CompositeId save(Tenant tenantCreate);

    boolean existsByCode(String code);

    boolean existsByExternalId(String externalId);

    Tenant findByCode(String code);

    Tenant findByExternalId(String externalId);

    List<Tenant> findAll();

    boolean update(Tenant tenant);

    boolean delete(CompositeId id);

}
