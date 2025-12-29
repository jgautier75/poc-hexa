package com.acme.jga.config;

/*@Configuration
@RequiredArgsConstructor*/
public class OpenTelemetryConfig {
/*-
    private final AppGenericProperties appGenericProperties;

    @Bean
    public Resource otelResource() {
        return Resource.getDefault().toBuilder()
                .put("service.name", appGenericProperties.getModuleName())
                .put("service.version", "1.0.0").build();
    }

    @Bean
    @Primary
    public SdkTracerProvider tracerProvider(Resource otelResource) {
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(appGenericProperties.getOtlpEndpoint()).build();
        return SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).setScheduleDelay(Duration.ofSeconds(appGenericProperties.getOtlpPushFrequency())).build())
                .setResource(otelResource)
                .build();
    }

    @Bean
    @Primary
    public SdkMeterProvider meterProvider(Resource otelResource) {
        OtlpGrpcMetricExporter metricExporter = OtlpGrpcMetricExporter.builder()
                .setEndpoint(appGenericProperties.getOtlpEndpoint()).build();
        return SdkMeterProvider.builder()
                .registerMetricReader(PeriodicMetricReader.builder(metricExporter).setInterval(Duration.ofSeconds(appGenericProperties.getOtlpPushFrequency())).build())
                .setResource(otelResource)
                .build();
    }

    @Bean
    @Primary
    public SdkLoggerProvider loggerProvider(Resource otelResource) {
        OtlpGrpcLogRecordExporter logRecordExporter = OtlpGrpcLogRecordExporter.builder()
                .setEndpoint(appGenericProperties.getOtlpEndpoint()).build();
        return SdkLoggerProvider.builder()
                .addLogRecordProcessor(BatchLogRecordProcessor.builder(logRecordExporter).setScheduleDelay(Duration.ofSeconds(appGenericProperties.getOtlpPushFrequency())).build())
                .addResource(otelResource)
                .build();
    }

    @Bean
    public OpenTelemetry openTelemetry(SdkTracerProvider tracerProvider, SdkMeterProvider sdkMeterProvider, SdkLoggerProvider sdkLoggerProvider) {
        return OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setMeterProvider(sdkMeterProvider)
                .setLoggerProvider(sdkLoggerProvider)
                .setPropagators(ContextPropagators.create(TextMapPropagator
                        .composite(W3CTraceContextPropagator.getInstance(),
                                W3CBaggagePropagator.getInstance())))
                .buildAndRegisterGlobal();
    }
*/
}
