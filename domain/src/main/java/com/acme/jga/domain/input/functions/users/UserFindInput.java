package com.acme.jga.domain.input.functions.users;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;

import java.util.List;

public interface UserFindInput {
    List<User> findAll(CompositeId tenantId, CompositeId organizationId) throws FunctionalException;
}
