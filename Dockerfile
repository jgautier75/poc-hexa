FROM debian:13.3 as NATIVEBUILDER
ARG MAVEN_ROOT
COPY ${MAVEN_ROOT}/settings.xml /root/.m2/settings.xml
COPY ${MAVEN_ROOT}/toolchains.xml /root/.m2/toolchains.xml
WORKDIR /opt
RUN cat ~/.m2/settings.xml
RUN apt update && apt install curl -y && apt-get install build-essential -y && apt-get install libz-dev
RUN curl -L "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.9%2B10/OpenJDK21U-jdk_x64_linux_hotspot_21.0.9_10.tar.gz" --output OpenJDK21U-jdk_x64_linux_hotspot_21.0.9_10.tar.gz
RUN curl -L "https://download.oracle.com/graalvm/25/latest/graalvm-jdk-25_linux-x64_bin.tar.gz" --output graalvm-jdk-25_linux-x64_bin.tar.gz
RUN curl -L "https://dlcdn.apache.org/maven/maven-3/3.9.12/binaries/apache-maven-3.9.12-bin.tar.gz" --output apache-maven-3.9.12-bin.tar.gz
RUN tar -zxf OpenJDK21U-jdk_x64_linux_hotspot_21.0.9_10.tar.gz
RUN tar -zxf graalvm-jdk-25_linux-x64_bin.tar.gz
RUN tar -zxf apache-maven-3.9.12-bin.tar.gz
RUN mkdir /java-sources
WORKDIR /java-sources
COPY . /java-sources
RUN export JAVA_HOME="/opt/graalvm-jdk-25.0.1+8.1" && export PATH=$PATH:$JAVA_HOME/bin && ls -al && /opt/apache-maven-3.9.12/bin/mvn clean install -DskipTests -Pnative

FROM redhat/ubi9-minimal:9.7
EXPOSE 8080/tcp
RUN mkdir -p /app/
COPY --from=NATIVEBUILDER /java-sources/app/target/app /app/poc-hexa-app
CMD ["/app/poc-hexa-app"]