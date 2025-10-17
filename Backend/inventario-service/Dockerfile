FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar archivos de proyecto
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Dar permisos
RUN chmod +x mvnw

# Construir JAR
RUN ./mvnw clean package -DskipTests

# Exponer puerto
EXPOSE 8082

# CMD dinámico que encuentra el JAR generado
CMD java -jar target/*.jar
