package com.acme.jga.spi.adapter.user;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.users.UserDeleteOutput;
import com.acme.jga.spi.dao.users.api.UsersDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDeleteOutputImpl implements UserDeleteOutput {
    private final UsersDao usersDao;

    public UserDeleteOutputImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public Integer delete(CompositeId tenantId, CompositeId organizationId, CompositeId id) {
        return usersDao.deleteUser(id, tenantId, organizationId);
    }
}
