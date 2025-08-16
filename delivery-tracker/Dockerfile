# ---- Build stage ----
FROM maven:3.9.11-eclipse-temurin-24 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=build /app/target/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
