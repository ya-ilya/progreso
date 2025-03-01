FROM gradle:jdk21-alpine AS build

WORKDIR /progreso-irc

COPY --chown=gradle:gradle . .

RUN --mount=type=cache,target=/root/.gradle gradle --no-daemon :progreso-irc:shadowJar || true

FROM openjdk:21-jdk-slim

RUN mkdir /app

COPY --from=build /progreso-irc/progreso-irc/build/libs/shadow.jar /app/application.jar

EXPOSE ${SERVER_PORT}

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "/app/application.jar"]