FROM docker.io/eclipse-temurin:21-alpine

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} backend-project.jar
COPY target/classes/git.json git.json

EXPOSE 8080/tcp

CMD ["java", "-XX:+UseG1GC", "-Dspring.profiles.active=deploy", "-jar", "backend-project.jar"]