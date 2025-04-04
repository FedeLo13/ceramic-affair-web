# Usa una imagen oficial de Java 17
FROM eclipse-temurin:17-jdk

# Crea el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia todos los archivos al contenedor
COPY . .

# Compila el proyecto con Maven, sin correr los tests
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Exponer el puerto 8080 para la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/ceramic_affair_web-0.0.1-SNAPSHOT.jar"]
