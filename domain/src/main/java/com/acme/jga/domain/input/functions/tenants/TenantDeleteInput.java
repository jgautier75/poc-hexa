package com.acme.jga.domain.input.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;

public interface TenantDeleteInput {

    boolean deleteTenant(String uid) throws FunctionalException;

}
