FROM openjdk:8-alpine

ARG MICRO_HOME=/opt/app/fproxy
ARG BIN_HOME=$MICRO_HOME/bin
ARG JAR_FILE=fproxy-exec.jar

RUN apk update && \
    apk add ca-certificates

## Setup JAVA_HOME, this is useful for docker commandline
ENV JAVA_HOME usr/lib/jvm/java-1.8-openjdk
RUN export JAVA_HOME

# Build up the deployment folder structure
RUN mkdir -p $MICRO_HOME
WORKDIR $MICRO_HOME
COPY maven/fproxy/ .
RUN chmod 755 $BIN_HOME/* && \
    mkdir /logs && \
    ln -s /logs $MICRO_HOME/logs
# Create the appuser
RUN addgroup --system appgroup && \
    adduser --system --uid 1001 --ingroup appgroup appuser && \
    chown -R appuser:appgroup $MICRO_HOME && \
    chmod 777 /logs
USER appuser

CMD ["/opt/app/fproxy/bin/start.sh"]
