# sudo docker build -t todo-zuul-server:v1.0.2 .

FROM openjdk:14.0.1-slim-buster
VOLUME /tmp
EXPOSE 8090
ADD ./target/zuul-server-1.0.1.jar zuul-server.jar
ENTRYPOINT ["java", "-jar", "/zuul-server.jar"]