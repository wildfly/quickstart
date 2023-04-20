function runPostHelmInstallCommands() {
    oc apply -f charts/opentelemetry-collector.yaml
}


function getMvnVerifyExtraArguments()
{
  mgmtHostRoute=$(oc get route otelcol-grpc --template='{{ .spec.host }}')
  echo "-Dopentelemetry.collector.host=https://opentelemetry"
}


function cleanPrerequisites()
{
  echo "Removing all opentelemetry-tracing resources"
  oc delete route otelcol-grpc
  oc delete route otelcol-prometheus
  oc delete service opentelemetrycollector
  oc delete deployment opentelemetrycollector
  oc delete configmap collector-config
}


function testsFailed() {
      echo "----> Getting status of all pods"
      oc get pods
      echo "----> Checking logs for postgres pod"
      echo oc logs todo-backend-postgresql-0
      echo "----> Checking events"
      oc get events
}
