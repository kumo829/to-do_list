# sudo docker build -t todo-user-account-service:v1.0.0 .
FROM openjdk:14.0.1-slim-buster
VOLUME /tmp
ADD ./target/user-account-service-1.0.0.jar user-account-service.jar
ENTRYPOINT ["java", "-jar", "/user-account-service.jar"]