FROM java:8
VOLUME /tmp
RUN echo "Asia/shanghai" > /etc/timezone;
ADD target/explore-0.0.1-SNAPSHOT.jar api.jar
RUN bash -c 'touch /api.jar'
EXPOSE 10002
ENTRYPOINT ["java","-jar","/api.jar"]