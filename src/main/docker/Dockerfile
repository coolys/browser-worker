FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    SIMLIFE_SLEEP=0 \
    JAVA_OPTS=""

RUN mkdir /var/log/service

RUN chmod 755 -R /var/log/service
VOLUME /var/log/service


RUN mkdir /var/log/data

RUN chmod 755 -R /var/log/data
VOLUME /var/log/data


# Add a simlife user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh simlife
WORKDIR /home/simlife

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown simlife:simlife entrypoint.sh
USER simlife

ADD *.war app.war

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8083

