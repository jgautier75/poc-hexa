package com.acme.jga.spi.adapter.user;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserCreateOutput;
import com.acme.jga.spi.dao.users.api.UsersDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCreateOutputImpl implements UserCreateOutput {
    private final UsersDao usersDao;

    public UserCreateOutputImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public CompositeId save(User user) throws FunctionalException {
        return usersDao.create(user);
    }
}
