package com.acme.jga.domain.input.functions.users;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.PaginatedResults;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.Map;

public interface UserFindInput {
    PaginatedResults<User> findAll(CompositeId tenantId, CompositeId organizationId, Map<SearchParams, Object> searchParams) throws FunctionalException;
}
