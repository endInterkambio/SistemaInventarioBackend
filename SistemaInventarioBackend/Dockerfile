# Etapa de build
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos archivos necesarios
COPY SistemaInventarioBackend/pom.xml .
COPY SistemaInventarioBackend/src ./src

# Construimos el JAR
RUN mvn clean package -DskipTests

# Etapa final
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiamos el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
