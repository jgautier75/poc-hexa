package com.acme.jga.domain.ports.secondary.events;

import com.acme.jga.domain.model.event.EventData;

public interface EventOutput {
    void saveChanges(EventData eventData);
}
