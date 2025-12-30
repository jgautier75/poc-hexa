package com.acme.jga.domain.output.functions.users;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;

public interface UserCreateOutput {
    CompositeId save(User user) throws FunctionalException;
}
