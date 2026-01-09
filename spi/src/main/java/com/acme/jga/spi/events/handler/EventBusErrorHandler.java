package com.acme.jga.spi.events.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Service
@RequiredArgsConstructor
public class EventBusErrorHandler implements ErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger("OTEL");

    @Override
    public void handleError(Throwable t) {
        LOGGER.error(this.getClass().getName(), t);
    }

}
