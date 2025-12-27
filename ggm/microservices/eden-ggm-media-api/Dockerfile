FROM java:8
ADD ./target/eden-ggm-media-api-0.0.1-SNAPSHOT.jar eden-ggm-media-api-0.0.1-SNAPSHOT.jar
RUN bash -c 'touch eden-ggm-media-api-0.0.1-SNAPSHOT.jar'
RUN mkdir -p /logdata
VOLUME /logdata
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar eden-ggm-media-api-0.0.1-SNAPSHOT.jar
EXPOSE 8080
