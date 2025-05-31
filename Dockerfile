FROM gradle:8.4.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

RUN mkdir -p /app/config

RUN mkdir -p /var/log/moco-api

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
