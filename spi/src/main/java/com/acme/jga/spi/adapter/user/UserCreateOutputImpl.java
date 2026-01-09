package com.acme.jga.spi.adapter.user;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.events.EventOutput;
import com.acme.jga.domain.output.functions.users.UserCreateOutput;
import com.acme.jga.spi.dao.users.api.UsersDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCreateOutputImpl implements UserCreateOutput {
    private final UsersDao usersDao;
    private final EventOutput eventOutput;

    public UserCreateOutputImpl(UsersDao usersDao, EventOutput eventOutput) {
        this.usersDao = usersDao;
        this.eventOutput = eventOutput;
    }

    @Override
    public CompositeId save(User user, EventData eventData) throws FunctionalException {
        CompositeId userId = usersDao.create(user);
        EventData updatedEvent = new EventData(eventData.contextUser(), userId.externalId(), eventData.scope(), eventData.auditAction(), eventData.target(), eventData.auditChanges());
        eventOutput.saveChanges(updatedEvent);
        return userId;
    }
}
