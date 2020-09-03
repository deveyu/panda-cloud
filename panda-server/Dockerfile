FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER yukongcode@gmail.com
VOLUME /tmp
ADD target/panda-server-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar' && bash -c echo '127.0.0.1 panda-server >> /etc/hosts'
EXPOSE 9000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]