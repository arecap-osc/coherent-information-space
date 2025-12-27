FROM java:8
ADD ./target/eden-intelligence-api-console-0.0.1-SNAPSHOT.jar eden-intelligence-api-console-0.0.1-SNAPSHOT.jar
RUN bash -c 'touch eden-intelligence-api-0.0.1-SNAPSHOT.jar'
RUN mkdir -p /logdata
VOLUME /logdata
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar eden-intelligence-api-console-0.0.1-SNAPSHOT.jar
EXPOSE 8080
