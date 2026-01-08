package com.acme.jga.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.time.ZoneOffset;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public JsonMapper objectMapper() {
        return JsonMapper.builder()
                .enable(tools.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.INDENT_OUTPUT)
                .defaultTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
                .changeDefaultPropertyInclusion(value -> {
                    value.withContentInclusion(JsonInclude.Include.NON_NULL);
                    value.withValueInclusion(JsonInclude.Include.NON_NULL);
                    return value;
                })
                .build();
    }

}
