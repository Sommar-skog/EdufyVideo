#ED-157-AA - ED-316-AA removed skipTests
#ED-164-SA
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn -B clean package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8383
ENTRYPOINT ["java", "-jar", "app.jar"]