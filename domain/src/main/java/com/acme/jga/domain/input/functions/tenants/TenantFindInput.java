package com.acme.jga.domain.input.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;

public interface TenantFindInput {
    Tenant findByCode(String code) throws FunctionalException;

    Tenant findById(CompositeId tenantId) throws FunctionalException;

}
