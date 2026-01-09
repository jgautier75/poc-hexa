package com.acme.jga.spi.adapter.user;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.events.EventOutput;
import com.acme.jga.domain.output.functions.users.UserDeleteOutput;
import com.acme.jga.spi.dao.users.api.UsersDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDeleteOutputImpl implements UserDeleteOutput {
    private final UsersDao usersDao;
    private final EventOutput eventOutput;

    public UserDeleteOutputImpl(UsersDao usersDao, EventOutput eventOutput) {
        this.usersDao = usersDao;
        this.eventOutput = eventOutput;
    }

    @Override
    public Integer delete(CompositeId tenantId, CompositeId organizationId, CompositeId id, EventData eventData) {
        eventOutput.saveChanges(eventData);
        return usersDao.deleteUser(id, tenantId, organizationId);
    }
}
