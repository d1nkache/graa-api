FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts /app/
COPY src /app/src/
COPY .env /app/.env
RUN gradle build -x test

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/graa-backend-0.0.1-SNAPSHOT.jar /app/graa-backend.jar
COPY --from=build /app/.env /app/.env
ENTRYPOINT ["java", "-jar", "/app/graa-backend.jar"]

EXPOSE 8000
