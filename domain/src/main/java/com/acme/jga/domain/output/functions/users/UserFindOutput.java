package com.acme.jga.domain.output.functions.users;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.List;
import java.util.Map;

public interface UserFindOutput {
    List<User> findAll(CompositeId tenantId, CompositeId organizationId, Map<SearchParams, Object> searchParams);

    Integer countAll(CompositeId tenantId, CompositeId organizationId, Map<SearchParams, Object> searchParams);

    User findById(CompositeId tenantId, CompositeId organizationId, CompositeId id);

    boolean emailUsed(String email);

    boolean loginUsed(String login);
}
