package com.acme.jga.spi.dao.users.api;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;

import java.util.List;

public interface UsersDao {
    CompositeId create(User user);

    boolean emailExists(String email);

    boolean loginExists(String login);

    User findById(CompositeId id, CompositeId tenantId, CompositeId organizationId);

    List<User> findAll(CompositeId tenantId, CompositeId organizationId);

    Integer update(User user);

    Integer deleteUser(CompositeId id, CompositeId tenantId, CompositeId organizationId);
}
