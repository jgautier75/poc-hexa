package com.acme.jga.spi.federation.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class KeycloakTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakTest.class);
    private static final String REALM_URI = "/realms/myrealm";

    @Container
    KeycloakContainer KEYCLOAK_CONTAINER = new KeycloakContainer("quay.io/keycloak/keycloak:26.6.1")
            .withAdminPassword("admin")
            .withAdminUsername("admin")
            .withRealmImportFiles("/myrealm-realm.json", "/myrealm-users-0.json")
            .withProviderClassesFrom("target/classes")
            .withLogConsumer(of -> {
                LOGGER.warn(of.getUtf8String());
            })
            .waitingFor(Wait.forHttp("/").forStatusCode(200));

    @BeforeEach
    public void startKeycloak() {
        // To enable logging, add the following variable argument to runtime: -Djava.util.logging.manager=org.jboss.logmanager.LogManager
        KEYCLOAK_CONTAINER.start();
    }

    @Test
    public void testAccessTokenGeneration() {
        String accessToken = getAccessToken();
        LOGGER.info("Access token: {}", accessToken);
        JwtDecoder jwtDecoder = getJwtDecoder(getAuthServerUrl());
        Jwt decoded = jwtDecoder.decode(accessToken);
        Map<String, Object> claims = decoded.getClaims();
        assertTrue(claims.keySet().contains("email"));
        assertEquals("myuser@test.fr", claims.get("email"));
        assertEquals("MyLastName", claims.get("family_name"));
        assertEquals("MyFirstName", claims.get("given_name"));
        LOGGER.info("Claims: {}", claims);
    }

    private String getAccessToken() {
        final String authServerUrl = getAuthServerUrl();
        return given().contentType(ContentType.URLENC)
                .formParams(Map.of(
                        "username", "myuser",
                        "password", "mypass",
                        "grant_type", "password",
                        "client_id", "myclient",
                        "scope", "openid email profile"))
                .post(authServerUrl + REALM_URI + "/protocol/openid-connect/token")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("access_token");
    }

    private String getAuthServerUrl() {
        assertTrue(KEYCLOAK_CONTAINER.isRunning());
        final String authServerUrl = KEYCLOAK_CONTAINER.getAuthServerUrl();
        LOGGER.info("Auth server url: {}", authServerUrl);
        return authServerUrl;
    }

    private JwtDecoder getJwtDecoder(String authServerUrl) {
        return JwtDecoders.fromIssuerLocation(authServerUrl + REALM_URI);
    }

}
