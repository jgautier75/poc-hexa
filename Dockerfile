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
ENV LOG_PATH=/app/logs
RUN native-builder-scripts/buildNative.sh

FROM redhat/ubi9-minimal:9.8
EXPOSE 8080/tcp
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
RUN mkdir -p /app/errors/
RUN groupadd -r -g 101 appgrp
RUN useradd -r -u 100 -g appgrp -m -s /sbin/nologin appusr
RUN chown appusr /app/
RUN chmod u+w /app/errors
RUN chmod u+w /app/logs
RUN chown appusr /app/errors
RUN chown appusr /app/logs
COPY --from=nativebuilder /java-sources/com.acme.jga.PocApplication /app/poc-hexa-app
RUN chmod u+x /app/poc-hexa-app
USER appusr
ENTRYPOINT ["/app/poc-hexa-app"]