package com.acme.jga.domain.output.functions.users;

import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.user.User;

public interface UserUpdateOutput {
    Integer update(User user, EventData eventData);
}
