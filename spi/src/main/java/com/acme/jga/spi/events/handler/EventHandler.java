package com.acme.jga.spi.events.handler;

import com.acme.jga.auditing.events.protobuf.Event;
import com.acme.jga.domain.model.event.AuditChange;
import com.acme.jga.domain.model.event.AuditEvent;
import com.acme.jga.domain.model.event.EventStatus;
import com.acme.jga.domain.shared.StreamUtil;
import com.acme.jga.spi.adapter.event.impl.EventAdapter;
import com.acme.jga.spi.config.KafkaProducerConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EventHandler implements MessageHandler, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger("OTEL");
    private final PublishSubscribeChannel eventAuditChannel;
    private final KafkaProducerConfig kafkaProducerConfig;
    private final KafkaTemplate<String, Event.AuditEventMessage> kakaTemplateAudit;
    private final JsonMapper jsonMapper;
    private final EventAdapter eventAdapter;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public EventHandler(PublishSubscribeChannel eventAuditChannel,
                        KafkaProducerConfig kafkaProducerConfig,
                        KafkaTemplate<String, Event.AuditEventMessage> kakaTemplateAudit,
                        JsonMapper jsonMapper,
                        EventAdapter eventAdapter) {
        this.eventAuditChannel = eventAuditChannel;
        this.kafkaProducerConfig = kafkaProducerConfig;
        this.kakaTemplateAudit = kakaTemplateAudit;
        this.jsonMapper = jsonMapper;
        this.eventAdapter = eventAdapter;
    }

    @Override
    public void afterPropertiesSet() {
        eventAuditChannel.subscribe(this);
    }

    @Override
    public void handleMessage(@NonNull Message<?> message) throws MessagingException {
        if (!isRunning.get()) {
            try {
                isRunning.set(true);
                List<AuditEvent> auditEvents = eventAdapter.findPendingEvents();
                List<String> successfullySent = new ArrayList<>();
                List<String> sendingFailed = new ArrayList<>();
                auditEvents.forEach(ae -> {
                    if (convertAndSend(ae)) {
                        successfullySent.add(ae.getUid());
                    } else {
                        sendingFailed.add(ae.getUid());
                    }
                });
                if (!successfullySent.isEmpty()) {
                    eventAdapter.updateEvents(successfullySent, EventStatus.PROCESSED);
                }
                if (!sendingFailed.isEmpty()) {
                    eventAdapter.updateEvents(sendingFailed, EventStatus.FAILED);
                }
            } finally {
                isRunning.set(false);
            }
        }
    }

    private boolean convertAndSend(AuditEvent auditEvent) {
        boolean success = true;
        try {
            Event.AuditEventMessage auditEventMessage = protobufConversion(auditEvent.getPayload());
            ProducerRecord<String, Event.AuditEventMessage> producerRecord = new ProducerRecord<>(kafkaProducerConfig.getTopicNameAuditEvents(), auditEvent.getObjectUid(), auditEventMessage);
            kakaTemplateAudit.send(producerRecord);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error converting event", e);
            success = false;
        }
        return success;
    }

    /**
     * Convert audit event to protobuf format.
     *
     * @param payload JSON Payload
     * @return Audit event in protobuf format
     */
    private Event.AuditEventMessage protobufConversion(String payload) throws JsonProcessingException {
        Event.AuditEventMessage.Builder auditEventMessageBuilder = Event.AuditEventMessage.newBuilder();
        AuditEvent auditEvent = jsonMapper.readValue(payload, AuditEvent.class);
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        auditEventMessageBuilder.setAction(Event.AuditAction.forNumber(auditEvent.getAction().ordinal()));
        auditEventMessageBuilder.setCreatedAt(auditEvent.getCreatedAt().atZone(ZoneOffset.UTC).format(isoFormatter));
        auditEventMessageBuilder.setLastUpdatedAt(auditEvent.getLastUpdatedAt().atZone(ZoneOffset.UTC).format(isoFormatter));
        auditEventMessageBuilder.setUid(auditEvent.getObjectUid());
        auditEventMessageBuilder.setObjectUid(auditEvent.getObjectUid());
        auditEventMessageBuilder.setStatus(auditEvent.getStatus().ordinal());
        if (auditEvent.getTarget() != null) {
            auditEventMessageBuilder.setTarget(Event.AuditTarget.forNumber(auditEvent.getTarget().ordinal()));
        }
        if (auditEvent.getAuthor() != null) {
            Event.AuditAuthor auditAuthor = Event.AuditAuthor.newBuilder().buildPartial();
            if (auditEvent.getAuthor().getName() != null) {
                auditAuthor.toBuilder().setName(auditEvent.getAuthor().getName());
            }
            if (auditEvent.getAuthor().getUid() != null) {
                auditAuthor.toBuilder().setUid(auditEvent.getAuthor().getUid());
            }
            auditEventMessageBuilder.setAuthor(auditAuthor).build();
        }
        convertScope(auditEvent, auditEventMessageBuilder);
        List<Event.AuditChange> evtChanges = StreamUtil.ofNullableList(auditEvent.getChanges()).map(EventHandler::convertAuditChange).toList();
        auditEventMessageBuilder.addAllChanges(evtChanges);
        return auditEventMessageBuilder.build();
    }

    private static Event.AuditChange convertAuditChange(AuditChange auditChange) {
        Event.AuditChange.Builder builder = Event.AuditChange.newBuilder();
        if (auditChange.getFrom() != null) {
            builder.setFrom(auditChange.getFrom());
        }
        if (auditChange.getTo() != null) {
            builder.setTo(auditChange.getTo());
        }
        if (auditChange.getObject() != null) {
            builder.setObject(auditChange.getObject());
        }
        if (auditChange.getOperation() != null) {
            builder.setOperation(auditChange.getOperation().name());
        }
        return builder.build();
    }

    private static void convertScope(AuditEvent auditEvent, Event.AuditEventMessage.Builder auditEventMessageBuilder) {
        if (auditEvent.getScope() != null) {
            Event.AuditScope.Builder auditScopeBuilder = Event.AuditScope
                    .newBuilder()
                    .setTenantUid(auditEvent.getScope().getTenantUid())
                    .setTenantCode(auditEvent.getScope().getTenantName());
            if (auditEvent.getScope().getOrganizationName() != null) {
                auditScopeBuilder.setOrganizationCode(auditEvent.getScope().getOrganizationName());
            }
            if (auditEvent.getScope().getOrganizationUid() != null) {
                auditScopeBuilder.setOrganizationUid(auditEvent.getScope().getOrganizationUid());
            }
            auditEventMessageBuilder.setScope(auditScopeBuilder.build());
        }
    }
}
