package com.acme.jga.domain.model.user;

import com.acme.jga.domain.model.generic.CompositeId;

public record User(CompositeId id, CompositeId tenantId, CompositeId organizationId, String login,
                   String firstName, String lastName, String middleName,
                   String email, UserStatus status, String notifEmail, String secrets) {
}
