FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/Eventify.jar /app/Eventify.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Eventify.jar"]
