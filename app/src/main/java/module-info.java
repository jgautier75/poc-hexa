module app {
    requires domain;
    requires spring.web;
    requires static lombok;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires crypto;
    requires spring.beans;
    requires spring.core;
    requires spring.vault.core;
    requires tools.jackson.databind;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.blackbird;
    requires spring.boot.security.oauth2.resource.server;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.security.crypto;
    requires spring.security.oauth2.jose;
    requires spring.security.web;
}