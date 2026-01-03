package com.acme.jga.spi.adapter.user;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserFindOutput;
import com.acme.jga.search.filtering.constants.SearchParams;
import com.acme.jga.spi.dao.users.api.UsersDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserFindOutputImpl implements UserFindOutput {
    private final UsersDao usersDao;

    public UserFindOutputImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public List<User> findAll(CompositeId tenantId, CompositeId organizationId, Map<SearchParams, Object> searchParams) {
        return usersDao.findAll(tenantId, organizationId, searchParams);
    }

    @Override
    public Integer countAll(CompositeId tenantId, CompositeId organizationId, Map<SearchParams, Object> searchParams) {
        return usersDao.countAll(tenantId, organizationId, searchParams);
    }
}
