package com.acme.jga.domain.input.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.ExternalId;

public interface TenantDeleteInput {

    boolean deleteTenant(CompositeId id) throws FunctionalException;

}
