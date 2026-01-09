package com.acme.jga.domain.functions.users.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.users.UserDeleteInput;
import com.acme.jga.domain.input.functions.users.UserFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserDeleteOutput;

@DomainService
public class UserDeleteFuncImpl implements UserDeleteInput {
    private final UserFindInput userFindInput;
    private final UserDeleteOutput userDeleteOutput;
    private final EventPublisher eventPublisher;

    public UserDeleteFuncImpl(UserFindInput userFindInput,
                              UserDeleteOutput userDeleteOutput,
                              EventPublisher eventPublisher) {
        this.userFindInput = userFindInput;
        this.userDeleteOutput = userDeleteOutput;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Integer delete(CompositeId tenantId, CompositeId organizationId, CompositeId id) throws FunctionalException {
        User byId = this.userFindInput.findById(tenantId, organizationId, id);
        Integer nbDeleted = userDeleteOutput.delete(byId.tenantId(), byId.organizationId(), byId.id());
        this.eventPublisher.pushAuditEvents();
        return nbDeleted;
    }
}
