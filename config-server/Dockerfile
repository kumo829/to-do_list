# sudo docker build -t todo-config-server:v1.0.0 .

FROM openjdk:14.0.1-slim-buster
VOLUME /tmp
EXPOSE 8888
ADD ./target/config-server-1.0.0.jar config-server.jar
ENTRYPOINT ["java", "-jar", "/config-server.jar"]