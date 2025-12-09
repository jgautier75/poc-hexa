package com.acme.jga.domain.output.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.tenant.Tenant;

public interface TenantUpdateOutput {
    boolean update(Tenant tenant) throws FunctionalException;
}
