function runPostHelmInstallCommands() {
    oc apply -f charts/management.yml
}


function getMvnVerifyExtraArguments()
{
  mgmtHostRoute=$(oc get route microprofile-health-management --template='{{ .spec.host }}')
  echo "-Dserver.management.host=https://${mgmtHostRoute}"
}


function cleanPrerequisites()
{
  echo "Removing microprofile-health-management service and route"
  oc delete route microprofile-health-management
  oc delete service microprofile-health-management
}
