# sudo docker build -t todo-user-service:v1.0.0 .
FROM openjdk:14.0.1-slim-buster
VOLUME /tmp
ADD ./target/user-service-1.0.0.jar user-service.jar
ENTRYPOINT ["java", "-jar", "/user-service.jar"]