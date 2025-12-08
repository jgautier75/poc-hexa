package com.acme.jga.domain.input.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantId;

public interface TenantFindInput {
    Tenant findByCode(String code) throws FunctionalException;

    Tenant findById(TenantId tenantId) throws FunctionalException;

}
