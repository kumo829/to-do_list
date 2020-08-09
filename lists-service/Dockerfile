# sudo docker build -t todo-list-service:v1.0.0 --force-rm=true .
FROM openjdk:14.0.1-slim-buster
VOLUME /tmp
ADD ./target/lists-service-1.0.0.jar lists-service.jar
ENTRYPOINT ["java", "-jar", "/lists-service.jar"]

