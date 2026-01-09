package com.acme.jga.domain.input.functions.users;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;

public interface UserDeleteInput {
    Integer delete(CompositeId tenantId, CompositeId organizationId, CompositeId id) throws FunctionalException;
}
