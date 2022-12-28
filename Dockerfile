# Build stage
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

# Jar stage
FROM openjdk:8-jdk-alpine
COPY --from=build /target/apispringboot-0.0.1-SNAPSHOT.jar apispringboot-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar","apispringboot-0.0.1-SNAPSHOT.jar"]
