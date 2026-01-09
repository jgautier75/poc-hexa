package com.acme.jga.spi.adapter.user;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.events.EventUpdateOutput;
import com.acme.jga.domain.output.functions.users.UserUpdateOutput;
import com.acme.jga.spi.dao.users.api.UsersDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserUpdateOutputImpl implements UserUpdateOutput {
    private final UsersDao usersDao;
    private final EventUpdateOutput eventUpdateOutput;

    public UserUpdateOutputImpl(UsersDao usersDao, EventUpdateOutput eventUpdateOutput) {
        this.usersDao = usersDao;
        this.eventUpdateOutput = eventUpdateOutput;
    }

    @Override
    public Integer update(User user, EventData eventData) {
        Integer nbUpdated = usersDao.update(user);
        eventUpdateOutput.saveChanges(eventData);
        return nbUpdated;
    }
}
