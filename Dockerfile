FROM alpine:latest as build

ADD https://download.java.net/java/GA/jdk14.0.1/664493ef4a6946b186ff29eb326336a2/7/GPL/openjdk-14.0.1_linux-x64_bin.tar.gz /opt/jdk/
RUN tar -xzvf /opt/jdk/openjdk-14.0.1_linux-x64_bin.tar.gz -C /opt/jdk/

RUN ["mkdir", "/jlinked"]

#RUN ["/opt/jdk/jdk-14.0.1/bin/jlink", "--compress=2", \
#     "--module-path", "/opt/jdk/jdk-14.0.1/jmods/", \
#     "--add-modules", "java.base", \
#     "--output", "/jlinked"]

RUN ["/opt/jdk/jdk-14.0.1/bin/jlink", "--no-header-files", "--no-man-pages", "--compress=2", "--strip-debug", "--add-modules", "java.base,java.logging,java.sql,java.naming,java.management,java.instrument,java.desktop,java.security.jgss", "--output", "/opt/lib/jvm/spring-boot-runtime"]

FROM alpine:latest
COPY --from=build /opt/jdk/jlinked /opt/jdk/
CMD ["/opt/jdk/bin/java", "--version"]
