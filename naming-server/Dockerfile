FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY ./pom.xml /app/pom.xml
RUN mvn -f /app/pom.xml dependency:go-offline -B

COPY . /app
RUN mvn -f /app/pom.xml clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
EXPOSE 8761
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
