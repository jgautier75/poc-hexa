package com.acme.jga.domain.output.functions.events;

import com.acme.jga.domain.model.event.EventData;

public interface EventUpdateOutput {
    void saveChanges(EventData eventData);
}
