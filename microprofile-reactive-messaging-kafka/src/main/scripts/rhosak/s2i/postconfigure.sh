#!/usr/bin/env bash

# This script (postconfigure.sh) is executed during launch of the application server (not during the build)
# This script is expected to be copied to $JBOSS_HOME/extensions/ folder by script install.sh

STANDALONE_XML='standalone-openshift.xml'
POSTCONFIGURE_PROPERTIES_FILE="${JBOSS_HOME}/extensions/cli.openshift.properties"

# On JBoss EAP with standalone-openshift.xml, on with WildFly standalone.xml
if [ ! -f "${JBOSS_HOME}/standalone/configuration/${STANDALONE_XML}" ]; then
  STANDALONE_XML='standalone.xml'
  sed -i "s/serverConfig=.*/serverConfig=${STANDALONE_XML}/" "${POSTCONFIGURE_PROPERTIES_FILE}"
fi

echo "Configuring server for use with RHOSAK"
echo "Executing microprofile-reactive-messaging-kafka ${JBOSS_HOME}/extensions/initialize-server.cli file with properties file: ${POSTCONFIGURE_PROPERTIES_FILE}."
[ "x$SCRIPT_DEBUG" = "xtrue" ] && cat "${JBOSS_HOME}/extensions/initialize-server.cli"
"${JBOSS_HOME}"/bin/jboss-cli.sh --file="${JBOSS_HOME}/extensions/initialize-server.cli" --properties="${POSTCONFIGURE_PROPERTIES_FILE}"

