FROM tomcat:10.1.40-jdk17

ARG PROJECT_JAR_VERSION
RUN echo ${PROJECT_JAR_VERSION}


ADD target/device-message-api.war /tmp/device-message.war
RUN ls -l /tmp/
COPY src/scripts/* /opt/device-message-api/bin/
ADD src/main/resources/application.properties /opt/device-message-api/application.properties
RUN mkdir /opt/logback
RUN mkdir /logs/
RUN mkdir /mqtt/
RUN chmod 755 /opt/logback
RUN chmod 777 /logs/
RUN chmod 777 /mqtt/
ADD src/main/resources/logback.xml /opt/logback/logback.xml


RUN chmod 755 /opt/device-message-api/bin/start.sh
RUN ls -l /tmp/
RUN rm -rf /usr/local/tomcat/webapps/*
RUN mv /tmp/device-message.war /usr/local/tomcat/webapps

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN chown -R appuser:appgroup /usr/local/tomcat
USER appuser
ENTRYPOINT /bin/sh /opt/device-message-api/bin/start.sh
