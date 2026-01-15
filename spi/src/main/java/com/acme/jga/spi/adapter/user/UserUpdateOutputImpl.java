package com.acme.jga.spi.adapter.user;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.events.EventOutput;
import com.acme.jga.domain.output.functions.users.UserUpdateOutput;
import com.acme.jga.spi.dao.users.api.UsersDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserUpdateOutputImpl implements UserUpdateOutput {
    private final UsersDao usersDao;
    private final EventOutput eventOutput;

    public UserUpdateOutputImpl(UsersDao usersDao, EventOutput eventOutput) {
        this.usersDao = usersDao;
        this.eventOutput = eventOutput;
    }

    @Override
    public Integer update(User user, EventData eventData) {
        Integer nbUpdated = usersDao.update(user);
        if (!eventData.auditChanges().isEmpty()) {
            eventOutput.saveChanges(eventData);
        }
        return nbUpdated;
    }
}
