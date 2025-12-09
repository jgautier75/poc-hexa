package com.acme.jga.config;

import com.acme.jga.PocApplication;
import com.acme.jga.domain.annotations.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackageClasses = {PocApplication.class},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class})})
public class AppConfig {

}
