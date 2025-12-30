package com.acme.jga.domain.output.functions.users;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;

import java.util.List;

public interface UserFindOutput {
    List<User> findAll(CompositeId tenantId, CompositeId organizationId);
}
