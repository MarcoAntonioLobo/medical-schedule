FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY wait-for-postgres.sh .
RUN chmod +x wait-for-postgres.sh

# Instala cliente psql
RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["./wait-for-postgres.sh", "postgres", "java", "-jar", "app.jar"]
