#!/usr/bin/env bash

# This script (postconfigure.sh) is executed during launch of the application server (not during the build)
# This script is expected to be copied to $JBOSS_HOME/extensions/ folder by script install.sh

STANDALONE_XML='standalone-openshift.xml'
POSTCONFIGURE_PROPERTIES_FILE="${JBOSS_HOME}/extensions/cli.openshift.properties"

# on JBoss EAP with standalone-openshift.xml, on with WildFly standalone.xml
if [ ! -f "${JBOSS_HOME}/standalone/configuration/${STANDALONE_XML}" ]; then
  STANDALONE_XML='standalone.xml'
  sed -i "s/serverConfig=.*/serverConfig=${STANDALONE_XML}/" "${POSTCONFIGURE_PROPERTIES_FILE}"
fi

# container does not provide PostgreSQL driver
if [ ! -f "${JBOSS_HOME}/modules/org/postgresql/jdbc/main/module.xml" ]; then
  echo "Creating PostgreSQL JDBC module and driver under ${STANDALONE_XML}"
  "${JBOSS_HOME}"/bin/jboss-cli.sh "embed-server --server-config=${STANDALONE_XML},\
    module add --name=org.postgresql.jdbc --module-xml=${JBOSS_HOME}/extensions/postgresql-module.xml"
  "${JBOSS_HOME}"/bin/jboss-cli.sh "embed-server --server-config=${STANDALONE_XML},\
    /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql.jdbc,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)"
fi

echo "Executing ejb-txn-remote-call/client ${JBOSS_HOME}/extensions/remote-configuration.cli file with properties file: ${POSTCONFIGURE_PROPERTIES_FILE}."
[ "x$SCRIPT_DEBUG" = "xtrue" ] && cat "${JBOSS_HOME}/extensions/remote-configuration.cli"
"${JBOSS_HOME}"/bin/jboss-cli.sh --file="${JBOSS_HOME}/extensions/remote-configuration.cli" --properties="${POSTCONFIGURE_PROPERTIES_FILE}"

echo "Using client.war instead of ROOT.war"
if [ -f "${JBOSS_HOME}"/standalone/deployments/ROOT.war ]; then
  mv "${JBOSS_HOME}"/standalone/deployments/ROOT.war "${JBOSS_HOME}"/standalone/deployments/client.war
fi