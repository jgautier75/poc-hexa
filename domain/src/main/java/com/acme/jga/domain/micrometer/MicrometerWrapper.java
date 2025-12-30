package com.acme.jga.domain.micrometer;

import io.micrometer.common.KeyValues;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.logs.Severity;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;

import java.time.Instant;
import java.time.ZoneId;

public class MicrometerWrapper {
    private static final String INSTRUMENTATION_NAME = "micrometer_loggin";
    private final ObservationRegistry observationRegistry;
    private final SdkLoggerProvider sdkLoggerProvider;

    public MicrometerWrapper(ObservationRegistry observationRegistry, SdkLoggerProvider sdkLoggerProvider) {
        this.observationRegistry = observationRegistry;
        this.sdkLoggerProvider = sdkLoggerProvider;
    }

    protected String nvl(String text) {
        return text != null ? text : "";
    }

    protected <T> T observe(String observationName, KeyValues keyValues, ObservableOperation<T> observableOperation) {
        Observation notStarted = Observation.createNotStarted(observationName, this.observationRegistry);
        notStarted.start();
        if (keyValues != null) {
            notStarted.highCardinalityKeyValues(keyValues);
        }
        try {
            return observableOperation.execute();
        } finally {
            notStarted.stop();
        }
    }

    protected void log(String message, Attributes attributes) {
        sdkLoggerProvider.get(INSTRUMENTATION_NAME)
                .logRecordBuilder()
                .setAllAttributes(attributes)
                .setBody(message)
                .setObservedTimestamp(Instant.now().atZone(ZoneId.of("UTC")).toInstant())
                .setSeverity(Severity.INFO).emit();
    }

}
