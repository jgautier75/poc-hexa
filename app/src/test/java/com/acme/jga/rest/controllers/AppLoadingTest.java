package com.acme.jga.rest.controllers;

import com.acme.jga.config.SecurityProperties;
import com.acme.jga.config.VaultSecrets;
import com.acme.jga.crypto.encode.CryptoEncoder;
import com.acme.jga.utils.DaoTestUtils;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureDataSourceInitialization;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategyTarget;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@AutoConfigureDataSourceInitialization
@Transactional
public class AppLoadingTest {
    private static final String OIDC_BASE_REALM_URI = "/realms/myrealm";
    private static final BasicCredentialsProvider basicAuthProvider = new BasicCredentialsProvider();

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private SecurityProperties securityProperties;

    @MockitoBean
    private VaultSecrets vaultSecrets;

    @MockitoBean
    private CryptoEncoder cryptoEngine;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DaoTestUtils.POSTGRESQL_VERSION);

    @Container
    public static GenericContainer<?> otlpContainer = new GenericContainer<>(DockerImageName.parse("otel/opentelemetry-collector-contrib:0.142.0"))
            .withExposedPorts(4317, 4318)
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("otel-collector-config.yml"),
                    "/etc/otel-collector-config.yml"
            )
            .withCommand("--config=/etc/otel-collector-config.yml")
            .waitingFor(Wait.forLogMessage(".*Everything is ready.*\\s", 1));

    @RegisterExtension
    private static final WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

    @BeforeAll
    public static void initKeycloakAndVault() {
        try {
            String oidcConfig = readResource("keycloak_oidc_config.json");
            wireMockServer.stubFor(get(OIDC_BASE_REALM_URI + "/.well-known/openid-configuration")
                    .willReturn(aResponse()
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBody(oidcConfig)));
            String certs = readResource("keycloak_certs.json");
            wireMockServer.stubFor(get(OIDC_BASE_REALM_URI + "/protocol/openid-connect/certs")
                    .willReturn(aResponse()
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBody(certs)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("management.otlp.metrics.export.enabled",() -> false);
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuerUri", () -> wireMockServer.baseUrl() + OIDC_BASE_REALM_URI);
        otlpContainer.start();
        Integer mappedHttpPort = otlpContainer.getMappedPort(4318);
        registry.add("management.opentelemetry.logging.export.otlp.endpoint", () -> "http://" + otlpContainer.getHost() + ":" + mappedHttpPort + "/v1/logs");
        registry.add("management.opentelemetry.tracing.export.otlp.endpoint", () -> "http://" + otlpContainer.getHost() + ":" + mappedHttpPort + "/v1/traces");
        registry.add("management.otlp.metrics.export.url", () -> "http://" + otlpContainer.getHost() + ":" + mappedHttpPort + "/v1/metrics");
    }

    @BeforeEach
    public void beforeTests() throws Exception {
        DaoTestUtils.performLiquibaseUpdate(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    }

    @Test
    void actuator() throws Exception {
        await().pollInterval(Duration.ofSeconds(2L)).atMost(Duration.ofSeconds(6L)).until(actuatorHttpStatus200());
    }

    /**
     * Fetch actuator url.
     *
     * @return Http status code
     */
    private Callable<Boolean> actuatorHttpStatus200() throws UnknownHostException {
        final AtomicInteger httpStatus = new AtomicInteger();
        String hostName = InetAddress.getLocalHost().getHostName();
        String actuatorUrl = "http://" + hostName + ":" + randomServerPort + "/poc-hexa/actuator";
        HttpHost targetHost = new HttpHost("http", hostName, randomServerPort);
        AuthScope authScope = new AuthScope(targetHost);
        basicAuthProvider.setCredentials(authScope, new UsernamePasswordCredentials(securityProperties.getUserName(), securityProperties.getPass().toCharArray()));

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(basicAuthProvider).disableAutomaticRetries().build()) {
            HttpGet httpGet = new HttpGet(actuatorUrl);
            httpStatus.set(httpClient.execute(httpGet, HttpResponse::getCode));
            log.info("Http response status: {}", httpStatus);
        } catch (Exception e) {
            log.error("Unable to query actuator", e);
        }
        return () -> httpStatus.get() == HttpStatus.OK.value();
    }

    private static String readResource(String resourceName) throws IOException {
        try (InputStream issuerStream = AppLoadingTest.class.getClassLoader().getResourceAsStream(resourceName)) {
            byte[] issuerBytes = issuerStream.readAllBytes();
            int wireMockPort = wireMockServer.getPort();
            return new String(issuerBytes).replaceAll("DYNAMIC_PORT", Integer.toString(wireMockPort));
        }
    }
}
