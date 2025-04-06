FROM gradle:8.4.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Create directory for configuration
RUN mkdir -p /app/config

# Create directory for logs
RUN mkdir -p /var/log/chatalgo-api

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.config.location=optional:file:/app/config/"]
