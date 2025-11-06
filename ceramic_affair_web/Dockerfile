# Etapa 1: Construir el JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q clean package -DskipTests

# Etapa 2: Imagen para ejecutar
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR construido desde la etapa de construcción
COPY --from=build /app/target/ceramic_affair_web-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]