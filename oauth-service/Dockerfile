# sudo docker build -t todo-oauth-service:v1.0.0 .
FROM openjdk:14.0.1-slim-buster
VOLUME /tmp
ADD ./target/oauth-service-1.0.0.jar oauth-service.jar
ENTRYPOINT ["java", "-jar", "/oauth-service.jar"]