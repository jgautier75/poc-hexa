package com.acme.jga.domain.input.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.tenant.TenantId;

public interface TenantDeleteInput {

    boolean deleteTenant(TenantId uid) throws FunctionalException;

}
