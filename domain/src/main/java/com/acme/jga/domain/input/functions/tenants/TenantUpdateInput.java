package com.acme.jga.domain.input.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.tenant.Tenant;

public interface TenantUpdateInput {

    boolean update(Tenant tenant) throws FunctionalException;
}
