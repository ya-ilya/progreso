FROM gradle:jdk25-alpine AS build

WORKDIR /progreso-irc

COPY --chown=gradle:gradle . .

RUN --mount=type=cache,target=/root/.gradle gradle --no-daemon :progreso-irc:shadowJar || true

FROM eclipse-temurin:25-jdk-alpine

RUN mkdir /app

COPY --from=build /progreso-irc/progreso-irc/build/libs/shadow.jar /app/application.jar

EXPOSE ${SERVER_PORT}

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "/app/application.jar"]