package com.acme.jga.spi.dao.tenants.api;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
import io.opentelemetry.api.trace.Span;

import java.util.List;

public interface TenantsDao {

    CompositeId save(Tenant tenantCreate);

    boolean existsById(CompositeId id);

    boolean existsByCode(String code);

    boolean existsByExternalId(String externalId, Span parentSpan) throws FunctionalException;

    Tenant findByCode(String code);

    Tenant findByExternalId(String externalId);

    Tenant findById(CompositeId id);

    List<Tenant> findAll();

    boolean update(Tenant tenant);

    boolean delete(CompositeId id);

}
