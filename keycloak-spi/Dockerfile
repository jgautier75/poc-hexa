FROM quay.io/keycloak/keycloak:26.5.6
COPY spi-federation/target/spi-federation-1.0.0-SNAPSHOT.jar /opt/keycloak/providers/spi-federation.jar
COPY spi-kafka/target/spi-kafka-1.0.0-SNAPSHOT.jar /opt/keycloak/providers/spi-kafka.jar
RUN /opt/keycloak/bin/kc.sh build