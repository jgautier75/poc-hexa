package com.acme.jga.domain.ports.primary.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;

public interface TenantDeleteInput {

    boolean deleteTenant(CompositeId id) throws FunctionalException;

}
