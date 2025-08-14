FROM openjdk:24-jdk-slim

ARG JAR_FILE=target/SistemaInventarioBackend-0.0.1.jar
COPY ${JAR_FILE} SistemaInventarioBackend.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "SistemaInventarioBackend.jar"]
