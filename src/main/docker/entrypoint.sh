#!/bin/sh

echo "The application will start in ${SIMLIFE_SLEEP}s..." && sleep ${SIMLIFE_SLEEP}
exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "${HOME}/app.war" "$@"
