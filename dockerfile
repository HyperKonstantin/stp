FROM docker.io/eclipse-temurin:21-alpine

WORKDIR /app

ARG JAR_FILE=target/*.jar
ENV SPRING_PROFILES_ACTIVE="dev"

COPY ${JAR_FILE} backend-project.jar
COPY target/classes/git.json git.json

EXPOSE 8080/tcp

CMD ["java", "-XX:+UseG1GC", "-jar", "backend-project.jar"]