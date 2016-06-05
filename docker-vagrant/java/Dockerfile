# TODO: tailor base image for each specific service, and expose the working directory, the Tomcat port, the BrowserSync ports

# Network interface issue is causing Xenial to fail in Docker: https://github.com/mitchellh/vagrant/pull/7241
#FROM FROM ubuntu:xenial64
FROM frolvlad/alpine-glibc:alpine-3.3_glibc-2.23

# see https://registry.hub.docker.com/u/frolvlad/alpine-oraclejdk8/
#FROM frolvlad/alpine-glibc:3.2

ENV ZULU_VERSION=8.13.0.5 \
    JAVA_VERSION=8 \
    JAVA_UPDATE=72 \
    JAVA_HOME=/usr/lib/jvm/default-jvm

COPY ./zulu*-jdk*-linux_x64.tar.gz.md5 \
     /tmp/

RUN apk add --no-cache --virtual=build-dependencies \
      ca-certificates \
      tar \
      wget && \
    \
    cd /tmp && \
    wget "http://cdn.azul.com/zulu/bin/zulu${ZULU_VERSION}-jdk${JAVA_VERSION}.0.${JAVA_UPDATE}-linux_x64.tar.gz" && \
    echo "$(cat zulu${ZULU_VERSION}-jdk${JAVA_VERSION}.0.${JAVA_UPDATE}-linux_x64.tar.gz.md5)  zulu${ZULU_VERSION}-jdk${JAVA_VERSION}.0.${JAVA_UPDATE}-linux_x64.tar.gz" \
      | md5sum -c - && \
    \
    mkdir -p "/usr/lib/jvm/java-${JAVA_VERSION}-zulu" && \
    tar xvf "zulu${ZULU_VERSION}-jdk${JAVA_VERSION}.0.${JAVA_UPDATE}-linux_x64.tar.gz" \
      -C "/usr/lib/jvm/java-${JAVA_VERSION}-zulu" \
      --strip-components=1 && \
    \
    ln -s "java-${JAVA_VERSION}-zulu" "${JAVA_HOME}" && \
    ln -s "${JAVA_HOME}/bin/"* /usr/bin/ && \
    \
    rm -rf \
      "${JAVA_HOME}/"*src.zip \
      "${JAVA_HOME}/demo" \
      "${JAVA_HOME}/sample" && \
    rm -rf \
      "${JAVA_HOME}/lib/missioncontrol" \
      "${JAVA_HOME}/lib/visualvm" \
      "${JAVA_HOME}/lib/"*javafx* && \
    rm -rf \
      "${JAVA_HOME}/jre/bin/javaws" \
      "${JAVA_HOME}/jre/lib/amd64/libdecora_sse.so" \
      "${JAVA_HOME}/jre/lib/amd64/"libprism_*.so \
      "${JAVA_HOME}/jre/lib/amd64/libfxplugins.so" \
      "${JAVA_HOME}/jre/lib/amd64/libglass.so" \
      "${JAVA_HOME}/jre/lib/amd64/libgstreamer-lite.so" \
      "${JAVA_HOME}/jre/lib/amd64/"libjavafx*.so \
      "${JAVA_HOME}/jre/lib/amd64/"libjfx*.so \
      "${JAVA_HOME}/jre/lib/ext/jfxrt.jar" \
      "${JAVA_HOME}/jre/lib/"deploy* \
      "${JAVA_HOME}/jre/lib/desktop" \
      "${JAVA_HOME}/jre/lib/javaws.jar" \
      "${JAVA_HOME}/jre/lib/plugin.jar" \
      "${JAVA_HOME}/jre/lib/"*javafx* \
      "${JAVA_HOME}/jre/lib/"*jfx* \
      "${JAVA_HOME}/jre/plugin" && \
    \
    apk del build-dependencies && \
    rm /tmp/*