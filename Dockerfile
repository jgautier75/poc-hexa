FROM debian:13.4 AS nativebuilder
ARG MAVEN_ROOT
COPY ${MAVEN_ROOT}/settings.xml /root/.m2/settings.xml
COPY ${MAVEN_ROOT}/template_toolchains.xml /root/.m2/template_toolchains.xml
WORKDIR /opt
RUN apt update && \
    apt install curl -y && \
    apt-get install build-essential -y && \
    apt-get install libz-dev && \
    apt-get install jq -y && \
    mkdir /java-sources
WORKDIR /java-sources
COPY . /java-sources
RUN native-builder-scripts/buildNative.sh

FROM redhat/ubi9-minimal:9.7
EXPOSE 8080/tcp
RUN mkdir -p /app/
RUN mkdir -p /app/errors/
COPY --from=nativebuilder /java-sources/com.acme.jga.PocApplication /app/poc-hexa-app
ENTRYPOINT ["/app/poc-hexa-app"]