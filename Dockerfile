FROM gradle:7.4.2-jdk17 AS build
WORKDIR /app
COPY . /app/
RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/Eventify-1.0-SNAPSHOT.jar /app/Eventify.jar

# Expose the port and run the application
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/Eventify.jar"]
