# ---- Build stage ----
FROM maven:3.9.11-eclipse-temurin-24 AS build
WORKDIR /app

# cache des deps en copiant d’abord le POM
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# puis le code
COPY src ./src
RUN mvn -q -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:24-jre
WORKDIR /app
# copie le JAR construit (wildcard OK s’il n’y a qu’un seul JAR)
COPY --from=build /app/target/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
