# syntax=docker/dockerfile:experimental
FROM openjdk:8-jdk-alpine3.9 AS build
WORKDIR /home/gradle/src

COPY . /home/gradle/src
RUN chmod +x gradlew
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build --no-watch-fs --no-daemon --no-build-cache --refresh-dependencies --no-configuration-cache

FROM openjdk:8-jre-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/message-board-backend.jar
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/message-board-backend.jar"]
