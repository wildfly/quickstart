function installPrerequisites()
{
  echo "Adding bitnami repository"
  helm repo add bitnami https://charts.bitnami.com/bitnami

  helm dependency update todo-backend-chart/
}

function helmInstall() {
    application="${1}"
    helm_set_arguments="$2"

    # TODO https://issues.redhat.com/browse/WFLY-18574 remove this when persistence is working
    helm_set_arguments="${helm_set_arguments} --set postgresql.primary.persistence.enabled=false"

    # Don't quote ${helm_set_arguments} as it breaks the command when empty, and seems to work without
    helm install "${application}" todo-backend-chart/   --wait --timeout="${helm_install_timeout}" ${helm_set_arguments}
    echo "$?"
}



function cleanPrerequisites()
{
  oc delete all -l template=postgresql-ephemeral-template
  oc delete secret todo-backend-db
  helm repo remove bitnami
}

function getHelmSetVariablePrefix() {
  echo "wildfly."
}

function helmInstallFailed() {
    echo "----> Getting status of all pods"
    oc get pods
    echo "----> Checking logs for postgres pod"
    oc logs todo-backend-postgresql-0
    echo "----> Checking events"
    oc get events
}