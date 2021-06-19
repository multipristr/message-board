# syntax=docker/dockerfile:experimental
FROM openjdk:8-jdk-alpine AS build
WORKDIR /home/gradle/src

COPY . /home/gradle/src
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build --parallel --configure-on-demand --watch-fs --daemon -Dorg.gradle.daemon.idletimeout=3600000 -Dorg.gradle.jvmargs=-Duser.language=en -Duser.region=US -Dfile.encoding=UTF-8 --no-build-cache --refresh-dependencies

FROM openjdk:8-jre-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/message-board-backend.jar
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/message-board-backend.jar"]
