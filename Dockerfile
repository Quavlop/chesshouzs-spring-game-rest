FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/server-0.0.1-SNAPSHOT.jar ./target/chesshouzs-spring-game-rest.jar

ENTRYPOINT ["java", "-jar", "./target/chesshouzs-spring-game-rest.jar"]
