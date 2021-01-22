FROM openjdk:11-jdk-slim
MAINTAINER Ramazan Sakin <ramazansakin63@gmail.com>
ADD target/*.jar user-service.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/user-service.jar"]