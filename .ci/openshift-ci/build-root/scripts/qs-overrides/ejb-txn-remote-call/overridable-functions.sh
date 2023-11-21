# These functions are 'overridable in the individual quickstarts.
# To do so create a ./qs-overrides/${qs_dir}/overridable-functions.sh and override the
# functions you need to. ${qs_dir} in this case is the same as the name of the quickstart directory
# that you want to tweak

# Installs any prerequisites before doing the Helm install.
# The current directory is the quickstart directory.
# The default is to use the quickstart directory as the name, but in some cases
# a quickstart may need to shorten the name of the application in order to control
# the length of the resources created by OpenShift
#
# Parameters
# 1 - the name of the qs directory (not the full path)
function applicationName() {
  #echo "${1}"
  # The fill microprofile-reactive-messaging-kafka name results in names of generated resources which are too long for
  # OpenShift to handle
  echo "ejb-txn-remote-call"
}


# Installs any prerequisites before doing the Helm install.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
function installPrerequisites()
{
  application="${1}"

  CURRENT_FOLDER=$(pwd)

  echo "Installing the PostgreSQL database"

  # Install bitnami and PostgreSQL
  helm repo add bitnami https://charts.bitnami.com/bitnami
  helm install postgresql bitnami/postgresql -f charts/postgresql.yaml --wait --timeout="${helm_install_timeout}"

  echo "Cloning WildFly operator repository in /tmp"

  cd /tmp || return
  git clone https://github.com/wildfly/wildfly-operator.git

  cd wildfly-operator || return

  echo "Installing and deploying the 'WildFly operator' to the cluster"

  oc adm policy add-cluster-role-to-user cluster-admin developer
  make install || return
  make deploy || return

  seconds=120
  now=$(date +%s)
  end=$(($seconds + $now))

  echo "Waiting for 2 minutes that the 'WildFly operator' pod is in the 'Running' status."
  while [ $now -lt $end ]; do
    sleep 5

    if [[ $(oc get pods --field-selector=status.phase==Running -l name=wildfly-operator | awk '{ if ($3 == "Running" && $2 == "1/1") { print } }') ]]; then
      break
    fi

    now=$(date +%s)
  done

  cd $CURRENT_FOLDER || return
}

function helmInstall() {
    helm_set_arguments="$2"

    # TODO https://issues.redhat.com/browse/WFLY-18574 remove this when persistence is working
    # This seems to work with my postgresql.yaml :fingers_crossed
    # helm_set_arguments="${helm_set_arguments} --set postgresql.primary.persistence.enabled=false"

    # Don't quote ${helm_set_arguments} as it breaks the command when empty, and seems to work without
    helm install client -f charts/client.yaml wildfly/wildfly --wait --timeout="${helm_install_timeout}" ${helm_set_arguments}
    echo "$?"
    # TODO: should we check when the build is done?
    helm install server -f charts/server.yaml wildfly/wildfly --wait --timeout="${helm_install_timeout}" ${helm_set_arguments}
    echo "$?"
    # TODO: should we check when the build is done?
}

# Commands to run once the Helm install has completed
function runPostHelmInstallCommands() {

    # Make sure that view permissions are granted to the default system account.
    oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default -n $(oc project -q)

    oc create -f client/client-cr.yaml
    # TODO: should we check when the deployment is completed?
    oc create -f server/server-cr.yaml
    # TODO: should we check when the deployment is completed?
}

# Cleans any prerequisites after doing the Helm uninstall.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
function cleanPrerequisites()
{
  cd /tmp/wildfly-operator || return

  make undeploy
  make uninstall

  cd ..
  rm -rf wildfly-operator

  # Uninstall PostgreSQL and remove bitnami
  helm uninstall postgresql
  helm repo remove bitnami
}
