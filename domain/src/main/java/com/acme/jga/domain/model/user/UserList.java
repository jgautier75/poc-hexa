package com.acme.jga.domain.model.user;

import com.acme.jga.domain.model.generic.CompositeId;

public record UserList(CompositeId id, CompositeId tenantId, CompositeId organizationId, String login,
                       String firstName, String lastName, String email, UserStatus status) {
}
