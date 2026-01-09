package com.acme.jga.spi.config;

import com.acme.jga.spi.events.handler.EventBusErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;

@Configuration
public class EventBusConfig {
    private final EventBusErrorHandler errorHandler;

    public EventBusConfig(EventBusErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Bean
    public PublishSubscribeChannel eventAuditChannel() {
        PublishSubscribeChannel exportChannel = new PublishSubscribeChannel();
        exportChannel.setErrorHandler(errorHandler);
        return exportChannel;
    }

}
