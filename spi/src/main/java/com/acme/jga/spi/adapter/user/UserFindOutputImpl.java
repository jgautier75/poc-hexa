package com.acme.jga.spi.adapter.user;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserFindOutput;
import com.acme.jga.spi.dao.users.api.UsersDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFindOutputImpl implements UserFindOutput {
    private final UsersDao usersDao;

    public UserFindOutputImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public List<User> findAll(CompositeId tenantId, CompositeId organizationId) {
        return usersDao.findAll(tenantId,organizationId);
    }
}
