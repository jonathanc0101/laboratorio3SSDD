# syntax=docker/dockerfile:1

#FROM eclipse-temurin:17-jdk-jammy

FROM maven:3.8.6-amazoncorretto-17

#RUN apt-get update
#RUN apt-get install -y --no-install-recommends apt-utils
#RUN apt-get install -y dialog
#RUN apt-get install -y wget unzip curl maven

WORKDIR bordero
COPY pom.xml ./
RUN mvn dependency:resolve
COPY src ./src
RUN mvn clean package

CMD mvn spring-boot:run
