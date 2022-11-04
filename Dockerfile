
FROM maven:3.8.6-amazoncorretto-17

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
CMD mvn spring-boot:run