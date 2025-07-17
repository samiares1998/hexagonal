FROM registry.gitlab.com/tenpo/docker-images/openjdk:openjdk21-1.0-alpine-grafana
ENV NEW_RELIC_APP_NAME bo-transaction-orchestrator
ENV OTEL_SERVICE_NAME bo-transaction-orchestrator
WORKDIR /app
COPY ./build/application/libs/app.jar /app/app.jar
ARG NEWRELIC_LICENSE_KEY
ENV NEW_RELIC_LICENSE_KEY ${NEWRELIC_LICENSE_KEY}
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
