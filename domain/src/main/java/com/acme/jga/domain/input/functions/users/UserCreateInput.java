package com.acme.jga.domain.input.functions.users;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;

public interface UserCreateInput {
    CompositeId create(User user) throws FunctionalException;
}
