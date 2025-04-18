# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /home/app

COPY . /home/app
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
EXPOSE 5000

COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
