        # sudo docker build -t todo-eureka-server:v1.0.0 .

FROM openjdk:14.0.1-slim-buster
VOLUME /tmp
EXPOSE 8761
ADD ./target/eureka-server-1.0.0.jar eureka-server.jar
ENTRYPOINT ["java", "-jar", "/eureka-server.jar"]
