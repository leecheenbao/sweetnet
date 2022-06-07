FROM openjdk:8
VOLUME /tmp
ADD target/sweet-net-docker.jar sweet-net-docker.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/sweet-net-docker.jar"]