package com.acme.jga.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.config")
@AllArgsConstructor(access = AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.NONE)
@Data
public class AppGenericProperties {
    private String moduleName;
    private String moduleVersion;
    private String errorPath;
    private String vaultPath;
}
