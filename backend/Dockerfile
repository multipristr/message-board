# syntax=docker/dockerfile:experimental
FROM eclipse-temurin:8u462-b08-jdk-jammy AS build
WORKDIR /home/gradle/src

COPY . /home/gradle/src
RUN chmod +x gradlew
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build --no-watch-fs --no-daemon --no-build-cache --refresh-dependencies --no-configuration-cache

FROM eclipse-temurin:8u462-b08-jre-jammy

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/message-board-backend.jar
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/message-board-backend.jar"]
