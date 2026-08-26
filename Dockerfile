# --- Etapa 1: build con Maven ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copiamos primero solo el pom.xml para cachear las dependencias
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Ahora el código fuente y compilamos el jar (sin correr tests para acelerar el build)
COPY src ./src
RUN mvn -B clean package -DskipTests

# --- Etapa 2: imagen final, solo el JRE + el jar ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
