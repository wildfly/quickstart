function runPostHelmInstallCommands() {
    kubectl apply -f charts/management-openshift.yml
}


function getMvnVerifyExtraArguments()
{
  mgmtHostRoute=$(oc get route microprofile-health-management --template='{{ .spec.host }}')
  echo "-Dserver.management.host=https://${mgmtHostRoute}"
}


function cleanPrerequisites()
{
  echo "Removing microprofile-health-management service and route"
  kubectl delete route microprofile-health-management
  kubectl delete service microprofile-health-management
}
