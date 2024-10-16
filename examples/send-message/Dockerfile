FROM maven:3.8.4-openjdk-17-slim AS build

COPY examples /home/app/libs/java/camunda-bpm-pubsub-parent/examples
COPY extension /home/app/libs/java/camunda-bpm-pubsub-parent/extension
COPY pom.xml /home/app/libs/java/camunda-bpm-pubsub-parent
RUN mvn -f /home/app/libs/java/camunda-bpm-pubsub-parent/pom.xml clean install

COPY examples/send-message/src /home/app/src
COPY examples/send-message/pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM openjdk:17-slim
ARG JAR_FILE=/home/app/target/*.jar
RUN apt-get update && apt-get install -y python3 python3-pip && ln -sf python3 /usr/bin/python
RUN pip3 install --no-cache --upgrade pip setuptools
RUN pip3 install envdir
RUN mkdir -p /run/secrets
COPY --from=build ${JAR_FILE} send-message.jar

ENTRYPOINT ["envdir", "/run/secrets", "java", "-jar", "/send-message.jar"]