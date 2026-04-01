package com.acme.jga.domain.ports.input.users;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.PaginatedResults;
import com.acme.jga.domain.model.metadata.EntityMetaData;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.Map;

public interface UserFindInput {
    PaginatedResults<User> findAll(CompositeId tenantId, CompositeId organizationId, Map<SearchParams, Object> searchParams) throws FunctionalException;

    User findById(CompositeId tenantId, CompositeId organizationId, CompositeId id) throws FunctionalException;

    User findBySingleCriteria(CompositeId tenantId, CompositeId organizationId, EntityMetaData searchKey) throws FunctionalException;
}
