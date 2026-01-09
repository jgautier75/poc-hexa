package com.acme.jga.spi.events.services;

import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.events.EventType;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherImpl implements EventPublisher {

    private final PublishSubscribeChannel publishSubscribeChannel;

    public EventPublisherImpl(PublishSubscribeChannel publishSubscribeChannel) {
        this.publishSubscribeChannel = publishSubscribeChannel;
    }

    @Override
    public void pushAuditEvents() {
        this.publishSubscribeChannel.send(MessageBuilder.withPayload(EventType.AUDIT_EVENTS.name()).build());
    }
}
