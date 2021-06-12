FROM adoptopenjdk/openjdk16:jdk-16.0.1_9-ubuntu-slim

RUN mkdir -p /app/config
ADD build/libs/*.jar /app/
ADD src/main/resources/application.yaml /app/config/application.yaml

WORKDIR /app

ENTRYPOINT java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/*.jar --spring.config.location=/app/config/application.yaml
